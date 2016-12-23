/*
 * Copyright (C) 2016 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.cnr.istc.ac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Network {

    private static final Logger LOG = Logger.getLogger(Network.class.getName());
    int n_bool_vars = 0;
    int n_arith_vars = 0;
    int n_slack_vars = 0;
    int n_enum_vars = 0;
    Map<Var<?>, Domain> domains = null;
    final Map<String, BoolVar> bool_vars = new HashMap<>();
    final Map<String, ArithVar> arith_vars = new HashMap<>();
    private final Map<Var<?>, Collection<Propagator>> watches = new IdentityHashMap<>();
    private final LinkedList<Var<?>> prop_q = new LinkedList<>();
    private final Map<Var<?>, Propagator> causes = new IdentityHashMap<>();
    private final LinkedList<Layer> layers = new LinkedList<>();
    final Map<ArithVar, Lin> tableau = new IdentityHashMap<>();
    private final Map<String, BoolVar> assertions = new HashMap<>();
    private final Set<BoolVar> unsat_core = new HashSet<>();

    //<editor-fold defaultstate="collapsed" desc="variable creation..">
    public BoolVar newBool() {
        BoolVar bv = new BoolVar(this, "b" + n_bool_vars++);
        bool_vars.put(bv.name, bv);
        return bv;
    }

    public BoolConst newBool(boolean val) {
        return new BoolConst(val ? LBool.L_TRUE : LBool.L_FALSE);
    }

    public ArithVar newReal() {
        ArithVar av = new ArithVar(this, "x" + n_arith_vars++);
        arith_vars.put(av.name, av);
        return av;
    }

    public ArithConst newReal(double val) {
        return new ArithConst(val);
    }

    @SafeVarargs
    public final <T> EnumVar<T> newEnum(T... allowed_vals) {
        return new EnumVar<>(this, "e" + n_enum_vars++, new EnumDomain<>(allowed_vals));
    }

    public <T> EnumConst<T> newEnum(T value) {
        return new EnumConst<>(value);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="sat constraints..">
    public BoolExpr not(BoolExpr expr) {
        if (expr instanceof BoolConst) {
            switch (((BoolConst) expr).val) {
                case L_TRUE:
                    return new BoolConst(LBool.L_FALSE);
                case L_FALSE:
                    return new BoolConst(LBool.L_TRUE);
                default:
                    throw new AssertionError(((BoolConst) expr).val.name());
            }
        } else {
            return new Not((BoolVar) expr.to_var(this));
        }
    }

    public BoolExpr and(BoolExpr... exprs) {
        return new And(Stream.of(exprs).map(expr -> expr.to_var(this)).toArray(BoolVar[]::new));
    }

    public BoolExpr or(BoolExpr... exprs) {
        return new Or(Stream.of(exprs).map(expr -> expr.to_var(this)).toArray(BoolVar[]::new));
    }

    public BoolExpr exct_one(BoolExpr... exprs) {
        return new ExctOne(Stream.of(exprs).map(expr -> expr.to_var(this)).toArray(BoolVar[]::new));
    }

    public BoolExpr imply(BoolExpr left, BoolExpr right) {
        return or(not(left), right);
    }

    public BoolExpr eq(BoolExpr left, BoolExpr right) {
        if (left instanceof BoolConst && right instanceof BoolConst) {
            return new BoolConst(((BoolConst) left).val == ((BoolConst) right).val ? LBool.L_TRUE : LBool.L_FALSE);
        } else if (left instanceof BoolConst) {
            return new BoolAssignment((BoolVar) right.to_var(this), ((BoolConst) left).val);
        } else if (right instanceof BoolConst) {
            return new BoolAssignment((BoolVar) left.to_var(this), ((BoolConst) right).val);
        } else {
            return new BoolEq((BoolVar) left.to_var(this), (BoolVar) right.to_var(this));
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="arithmetic constraints..">
    public ArithExpr minus(ArithExpr expr) {
        System.out.println("computing -" + expr.id());
        if (expr instanceof ArithConst) {
            return new ArithConst(-((ArithConst) expr).val);
        } else {
            Lin lin = new Lin();
            if (expr instanceof ArithConst) {
                lin.subtract(((ArithConst) expr).val);
            } else if (expr instanceof ArithVar) {
                if (tableau.containsKey(expr)) {
                    lin.subtract(tableau.get(expr));
                } else {
                    lin.subtract((ArithVar) expr);
                }
            } else if (expr instanceof Lin) {
                lin.subtract((Lin) expr);
            } else {
                throw new UnsupportedOperationException("non-linear expression..");
            }
            return lin;
        }
    }

    public ArithExpr sum(ArithExpr... exprs) {
        System.out.println("computing " + Stream.of(exprs).map(expr -> expr.id()).collect(Collectors.joining(" + ")));
        if (Stream.of(exprs).allMatch(expr -> expr instanceof ArithConst)) {
            return new ArithConst(Stream.of(exprs).mapToDouble(expr -> ((ArithConst) expr).val).sum());
        } else {
            Lin lin = new Lin();
            for (ArithExpr expr : exprs) {
                if (expr instanceof ArithConst) {
                    lin.add(((ArithConst) expr).val);
                } else if (expr instanceof ArithVar) {
                    if (tableau.containsKey(expr)) {
                        lin.add(tableau.get(expr));
                    } else {
                        lin.add((ArithVar) expr);
                    }
                } else if (expr instanceof Lin) {
                    lin.add((Lin) expr);
                } else {
                    throw new UnsupportedOperationException("non-linear expression..");
                }
            }
            return lin;
        }
    }

    public ArithExpr sub(ArithExpr... exprs) {
        System.out.println("computing " + Stream.of(exprs).map(expr -> expr.id()).collect(Collectors.joining(" - ")));
        ArithExpr[] c_exprs = new ArithExpr[exprs.length];
        for (int i = 0; i < c_exprs.length; i++) {
            c_exprs[i] = i == 0 ? exprs[i] : minus(exprs[i]);
        }
        return sum(c_exprs);
    }

    public ArithExpr mult(ArithExpr... exprs) {
        System.out.println("computing " + Stream.of(exprs).map(expr -> expr.id()).collect(Collectors.joining(" * ")));
        if (Stream.of(exprs).allMatch(expr -> expr instanceof ArithConst)) {
            return new ArithConst(Stream.of(exprs).mapToDouble(expr -> ((ArithConst) expr).val).reduce(1, (a, b) -> a * b));
        } else {
            double k = 1;
            Lin lin = new Lin();
            for (ArithExpr expr : exprs) {
                if (expr instanceof ArithConst) {
                    k *= ((ArithConst) expr).val;
                } else if (expr instanceof ArithVar) {
                    if (!lin.vars.isEmpty()) {
                        throw new UnsupportedOperationException("non-linear expression..");
                    }
                    if (tableau.containsKey(expr)) {
                        lin.add(tableau.get(expr));
                    } else {
                        lin.add((ArithVar) expr);
                    }
                } else if (expr instanceof Lin) {
                    if (!lin.vars.isEmpty()) {
                        throw new UnsupportedOperationException("non-linear expression..");
                    }
                    lin.add((Lin) expr);
                } else {
                    throw new UnsupportedOperationException("non-linear expression..");
                }
            }
            return lin.multiply(k);
        }
    }

    public ArithExpr div(ArithExpr left, ArithExpr right) {
        System.out.println("computing " + left.id() + " / " + right.id());
        if (right instanceof ArithConst) {
            if (left instanceof ArithConst) {
                return new ArithConst(((ArithConst) left).val / ((ArithConst) right).val);
            } else {
                return mult(newReal(1 / ((ArithConst) right).val), left);
            }
        } else {
            throw new UnsupportedOperationException("non-linear expression..");
        }
    }

    public BoolExpr leq(ArithExpr left, ArithExpr right) {
        System.out.println("computing " + left.id() + " <= " + right.id());
        if (left instanceof ArithConst && right instanceof ArithConst) {
            return new BoolConst(((ArithConst) left).val <= ((ArithConst) right).val ? LBool.L_TRUE : LBool.L_FALSE);
        } else {
            ArithExpr c_expr = sum(left, minus(right));
            if (c_expr instanceof Lin) {
                Lin lin = (Lin) c_expr;
                if (lin.vars.isEmpty()) {
                    return new BoolConst(lin.known_term <= 0 ? LBool.L_TRUE : LBool.L_FALSE);
                }
                double c_right = -lin.known_term;
                lin.known_term = 0;
                ArithVar c_left = (ArithVar) lin.to_var(this);
                return new ArithLEq(c_left, c_right);
            } else {
                throw new UnsupportedOperationException("non-linear expression..");
            }
        }
    }

    public BoolExpr eq(ArithExpr left, ArithExpr right) {
        System.out.println("computing " + left.id() + " == " + right.id());
        if (left instanceof ArithConst && right instanceof ArithConst) {
            return new BoolConst(((ArithConst) left).val == ((ArithConst) right).val ? LBool.L_TRUE : LBool.L_FALSE);
        } else {
            ArithExpr c_expr = sum(left, minus(right));
            if (c_expr instanceof Lin) {
                Lin lin = (Lin) c_expr;
                if (lin.vars.isEmpty()) {
                    return new BoolConst(lin.known_term == 0 ? LBool.L_TRUE : LBool.L_FALSE);
                }
                double c_right = -lin.known_term;
                lin.known_term = 0;
                ArithVar c_left = (ArithVar) lin.to_var(this);
                return new ArithEq(c_left, c_right);
            } else {
                throw new UnsupportedOperationException("non-linear expression..");
            }
        }
    }

    public BoolExpr geq(ArithExpr left, ArithExpr right) {
        System.out.println("computing " + left.id() + " >= " + right.id());
        if (left instanceof ArithConst && right instanceof ArithConst) {
            return new BoolConst(((ArithConst) left).val >= ((ArithConst) right).val ? LBool.L_TRUE : LBool.L_FALSE);
        } else {
            ArithExpr c_expr = sum(left, minus(right));
            if (c_expr instanceof Lin) {
                Lin lin = (Lin) c_expr;
                if (lin.vars.isEmpty()) {
                    return new BoolConst(lin.known_term >= 0 ? LBool.L_TRUE : LBool.L_FALSE);
                }
                double c_right = -lin.known_term;
                lin.known_term = 0;
                ArithVar c_left = (ArithVar) lin.to_var(this);
                return new ArithGEq(c_left, c_right);
            } else {
                throw new UnsupportedOperationException("non-linear expression..");
            }
        }
    }

    public double evaluate(ArithExpr expr) {
        if (expr instanceof ArithConst) {
            return ((ArithConst) expr).val;
        } else if (expr instanceof ArithVar) {
            return ((ArithVar) expr).val;
        } else if (expr instanceof Lin) {
            return ((Lin) expr).vars.entrySet().stream().mapToDouble(entry -> entry.getKey().val * entry.getValue()).sum() + ((Lin) expr).known_term;
        } else {
            throw new UnsupportedOperationException("non-linear expression..");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="enum constraints..">
    public <T> BoolExpr eq(Expr<EnumDomain<T>> left, Expr<EnumDomain<T>> right) {
        if (left instanceof EnumConst && right instanceof EnumConst) {
            return new BoolConst(((EnumConst<T>) left).val == ((EnumConst<T>) right).val ? LBool.L_TRUE : LBool.L_FALSE);
        } else if (left instanceof EnumConst) {
            return new EnumAssignment<>((EnumVar<T>) right.to_var(this), ((EnumConst<T>) left).val);
        } else if (right instanceof EnumConst) {
            return new EnumAssignment<>((EnumVar<T>) left.to_var(this), ((EnumConst<T>) right).val);
        } else {
            return new EnumEq<>((EnumVar<T>) left.to_var(this), (EnumVar<T>) right.to_var(this));
        }
    }

    public <T> BoolExpr eq(Expr<EnumDomain<T>> left, T right) {
        if (left instanceof EnumConst) {
            return new BoolConst(((EnumConst<T>) left).val == right ? LBool.L_TRUE : LBool.L_FALSE);
        } else {
            return new EnumAssignment<>((EnumVar<T>) left.to_var(this), right);
        }
    }
    //</editor-fold>

    public void add(BoolExpr... exprs) {
        assert exprs.length > 0;
        assert Stream.of(exprs).noneMatch(Objects::isNull);
        for (BoolExpr expr : exprs) {
            boolean intersect = ((BoolVar) expr.to_var(this)).intersect(LBool.L_TRUE, null);
            assert intersect;
        }
    }

    public boolean propagate() {
        unsat_core.clear();

        while (!prop_q.isEmpty()) {
            Var<?> var = prop_q.poll();
            Propagator cause = causes.remove(var);
            for (Propagator prop : watches.get(var)) {
                if (prop != cause) {
                    if (!prop.propagate(var)) {
                        prop_q.clear();
                        causes.clear();
                        if (unsat_core.isEmpty()) {
                            Set<Var<?>> viewed = new HashSet<>();
                            LinkedList<Var<?>> queue = new LinkedList<>();
                            queue.addAll(Arrays.asList(prop.getArgs()));
                            while (!queue.isEmpty()) {
                                Var<?> p = queue.pollFirst();
                                if (!viewed.contains(p)) {
                                    viewed.add(p);
                                    BoolVar dv = getDecisionVariable(p);
                                    if (dv != null) {
                                        unsat_core.add(dv);
                                    }
                                    if (layers.getLast().reason.containsKey(p)) {
                                        queue.addAll(layers.getLast().reason.get(p).stream().flatMap(c_prop -> Stream.of(c_prop.getArgs())).collect(Collectors.toList()));
                                    }
                                }
                            }
                        } else {
                            Set<BoolVar> c_unsat_core = new HashSet<>();
                            for (BoolVar q : unsat_core) {
                                BoolVar dv = getDecisionVariable(q);
                                if (dv != null) {
                                    c_unsat_core.add(dv);
                                }
                            }
                            unsat_core.clear();
                            unsat_core.addAll(c_unsat_core);
                        }
                        return false;
                    }
                }
            }
            if (rootLevel() && var.isSingleton()) {
                watches.remove(var);
            }
        }

        return true;
    }

    private BoolVar getDecisionVariable(Var<?> var) {
        ListIterator<Layer> li = layers.listIterator(layers.size());
        while (li.hasPrevious()) {
            Layer layer = li.previous();
            if (layer.reason.containsKey(var)) {
                switch (layer.decision_variable.domain) {
                    case L_TRUE:
                        return layer.decision_variable;
                    case L_FALSE:
                        return (BoolVar) not(layer.decision_variable).to_var(this);
                    default:
                        throw new AssertionError(layer.decision_variable.domain.name());
                }
            }
        }
        return null;
    }

    public boolean assign(BoolExpr var) {
        assert var.evaluate() == LBool.L_UNKNOWN;
        if (!prop_q.isEmpty() && !propagate()) {
            return false;
        }
        push();
        BoolVar bv = (BoolVar) var.to_var(this);
        boolean intersect = bv.intersect(LBool.L_TRUE, null);
        assert intersect;
        layers.getLast().decision_variable = bv;
        boolean propagate = propagate();
        if (!propagate) {
            pop();
        }
        return propagate;
    }

    public boolean rootLevel() {
        return layers.isEmpty();
    }

    public void push() {
        assert prop_q.isEmpty();
        Layer layer = new Layer(domains);
        domains = new IdentityHashMap<>();
        layers.add(layer);
    }

    public void pop() {
        assert prop_q.isEmpty();
        for (Var<?> var : domains.keySet()) {
            var.restore();
        }
        Layer layer = layers.getLast();
        domains = layer.domains;
        layers.pollLast();
    }

    public void store(Propagator prop) {
        for (Var<?> arg : prop.getArgs()) {
            if (!arg.isSingleton()) {
                if (!watches.containsKey(arg)) {
                    watches.put(arg, new LinkedList<>());
                }
                watches.get(arg).add(prop);
            }
        }
    }

    public void forget(Propagator prop) {
        for (Var<?> arg : prop.getArgs()) {
            if (!arg.isSingleton()) {
                if (watches.containsKey(arg)) {
                    boolean remove = watches.get(arg).remove(prop);
                    assert remove;
                    if (watches.get(arg).isEmpty()) {
                        watches.remove(arg);
                    }
                }
            }
        }
    }

    <D extends Domain> void enqueue(Var<D> var, D old_domain, Propagator prop) {
        if (!layers.isEmpty()) {
            if (!domains.containsKey(var)) {
                domains.put(var, old_domain);
            }
            if (!(var instanceof ArithVar) && prop != null) {
                if (!layers.getLast().reason.containsKey(var)) {
                    layers.getLast().reason.put(var, new ArrayList<>(Arrays.asList(prop)));
                } else {
                    layers.getLast().reason.get(var).add(prop);
                }
            }
        }
        if (watches.containsKey(var) && !causes.containsKey(var)) {
            prop_q.add(var);
            causes.put(var, prop);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="simplex..">
    private void update(ArithVar x_i, double v) {
        System.out.println("assigning " + v + " to " + x_i);
        assert !tableau.containsKey(x_i) : "x_i is a non-basic variable..";
        for (Map.Entry<ArithVar, Lin> entry : tableau.entrySet()) {
            if (entry.getValue().vars.containsKey(x_i)) {
                System.out.println(entry.getKey().name + " " + entry.getKey().domain + " " + entry.getKey().val + " <- " + (entry.getKey().val + entry.getValue().vars.get(x_i) * (v - x_i.val)));
                entry.getKey().val += entry.getValue().vars.get(x_i) * (v - x_i.val);
            }
        }
        System.out.println(x_i.name + " " + x_i.domain + " " + x_i.val + " <- " + v);
        x_i.val = v;
    }

    private void pivotAndUpdate(ArithVar x_i, ArithVar x_j, double v) {
        System.out.println("assigning " + v + " to " + x_i + " and pivoting " + x_i + " with " + x_j);
        assert tableau.containsKey(x_i) : "x_i is a basic variable..";
        assert !tableau.containsKey(x_j) : "x_j is a non-basic variable..";
        assert tableau.get(x_i).vars.containsKey(x_j);
        double theta = (v - x_i.val) / tableau.get(x_i).vars.get(x_j);
        System.out.println(x_i.name + " " + x_i.domain + " " + x_i.val + " <- " + v);
        x_i.val = v;
        System.out.println(x_j.name + " " + x_j.domain + " " + x_j.val + " <- " + (x_j.val + theta));
        x_j.val += theta;
        for (Map.Entry<ArithVar, Lin> entry : tableau.entrySet()) {
            if (entry.getKey() != x_i && entry.getValue().vars.containsKey(x_j)) {
                System.out.println(entry.getKey().name + " " + entry.getKey().domain + " " + entry.getKey().val + " <- " + (entry.getKey().val + entry.getValue().vars.get(x_j) * theta));
                entry.getKey().val += entry.getValue().vars.get(x_j) * theta;
            }
        }

        // we pivot x_i with x_j: x_i leaves the base while x_j enters the base..
        Lin lin_expr = tableau.remove(x_i);
        Double c_v = lin_expr.vars.remove(x_j);
        lin_expr.divide(-c_v);
        lin_expr.vars.put(x_i, 1.0 / c_v);
        for (Map.Entry<ArithVar, Lin> entry : tableau.entrySet()) {
            if (entry.getValue().vars.containsKey(x_j)) {
                Double c_c_v = entry.getValue().vars.remove(x_j);
                entry.getValue().add(new Lin(lin_expr).multiply(c_c_v));
            }
        }

        // we store the new variable into the current tableau..
        tableau.put(x_j, lin_expr);
    }

    private boolean check() {
        while (true) {
            Optional<ArithVar> opt_x_i = tableau.keySet().stream().sorted((ArithVar v0, ArithVar v1) -> v0.name.compareTo(v1.name)).filter(v -> (v.val < v.domain.lb || v.val > v.domain.ub)).findAny();
            if (!opt_x_i.isPresent()) {
                return true;
            }
            final ArithVar x_i = opt_x_i.get();
            if (x_i.val < x_i.domain.lb) {
                Optional<ArithVar> opt_x_j = tableau.get(x_i).vars.keySet().stream().sorted((ArithVar v0, ArithVar v1) -> v0.name.compareTo(v1.name)).filter(v -> ((tableau.get(x_i).vars.get(v) > 0 && v.val < v.domain.lb) || (tableau.get(x_i).vars.get(v) < 0 && v.val > v.domain.ub))).findAny();
                if (opt_x_j.isPresent()) {
                    pivotAndUpdate(x_i, opt_x_j.get(), x_i.domain.lb);
                } else {
                    // we generate an explanation for the conflict..
                    for (Map.Entry<ArithVar, Double> entry : tableau.get(x_i).vars.entrySet()) {
                        if (entry.getValue() > 0) {
                            unsat_core.add(assertions.get(entry.getKey().name + " <= " + entry.getKey().domain.ub));
                        } else if (entry.getValue() < 0) {
                            unsat_core.add(assertions.get(entry.getKey().name + " >= " + entry.getKey().domain.ub));
                        }
                    }
                    unsat_core.add(assertions.get(x_i.name + " >= " + x_i.domain.lb));
                    return false;
                }
            }
            if (x_i.val > x_i.domain.ub) {
                Optional<ArithVar> opt_x_j = tableau.get(x_i).vars.keySet().stream().sorted((ArithVar v0, ArithVar v1) -> v0.name.compareTo(v1.name)).filter(v -> ((tableau.get(x_i).vars.get(v) < 0 && v.val < v.domain.ub) || (tableau.get(x_i).vars.get(v) > 0 && v.val > v.domain.lb))).findAny();
                if (opt_x_j.isPresent()) {
                    pivotAndUpdate(x_i, opt_x_j.get(), x_i.domain.ub);
                } else {
                    // we generate an explanation for the conflict..
                    for (Map.Entry<ArithVar, Double> entry : tableau.get(x_i).vars.entrySet()) {
                        if (entry.getValue() > 0) {
                            unsat_core.add(assertions.get(entry.getKey().name + " >= " + entry.getKey().domain.lb));
                        } else if (entry.getValue() < 0) {
                            unsat_core.add(assertions.get(entry.getKey().name + " <= " + entry.getKey().domain.ub));
                        }
                    }
                    unsat_core.add(assertions.get(x_i.name + " <= " + x_i.domain.ub));
                    return false;
                }
            }
        }
    }

    boolean assertUpper(ArithVar x_i, double val, BoolVar geq_var, Propagator prop) {
        if (val >= x_i.domain.ub) {
            return true;
        } else if (val < x_i.domain.lb) {
            return false;
        } else {
            assertions.put(x_i.name + " <= " + val, geq_var);
            boolean intersect = x_i.intersect(new Interval(Double.NEGATIVE_INFINITY, val), prop);
            assert intersect;
            if (x_i.val > val) {
                if (!tableau.containsKey(x_i)) {
                    update(x_i, val);
                }
                return check();
            }
            return true;
        }
    }

    boolean assertLower(ArithVar x_i, double val, BoolVar leq_var, Propagator prop) {
        if (val <= x_i.domain.lb) {
            return true;
        } else if (val > x_i.domain.ub) {
            return false;
        } else {
            assertions.put(x_i.name + " >= " + val, leq_var);
            boolean intersect = x_i.intersect(new Interval(val, Double.POSITIVE_INFINITY), prop);
            assert intersect;
            if (x_i.val < val) {
                if (!tableau.containsKey(x_i)) {
                    update(x_i, val);
                }
                return check();
            }
            return true;
        }
    }

    public Collection<BoolVar> getUnsatCore() {
        return Collections.unmodifiableCollection(unsat_core);
    }
    //</editor-fold>

    private static class Layer {

        private BoolVar decision_variable;
        private final Map<Var<?>, Domain> domains;
        private final Map<Var<?>, Collection<Propagator>> reason = new IdentityHashMap<>();

        private Layer(Map<Var<?>, Domain> domains) {
            this.domains = domains;
        }
    }
}
