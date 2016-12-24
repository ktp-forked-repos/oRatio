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
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Lin implements ArithExpr {

    final Map<ArithVar, Double> vars;
    double known_term;

    public Lin() {
        this.vars = new IdentityHashMap<>();
        this.known_term = 0;
    }

    public Lin(double known_term) {
        this.vars = new IdentityHashMap<>();
        this.known_term = known_term;
    }

    public Lin(ArithVar var, double c) {
        this.vars = new IdentityHashMap<>();
        this.vars.put(var, c);
        this.known_term = 0;
    }

    public Lin(Lin expr) {
        this.vars = new IdentityHashMap<>(expr.vars);
        this.known_term = expr.known_term;
    }

    @Override
    public String id() {
        String lin = new String();
        ArithVar[] c_vs = vars.keySet().toArray(new ArithVar[vars.size()]);
        Arrays.sort(c_vs, (ArithVar v0, ArithVar v1) -> v0.name.compareTo(v1.name));
        for (int i = 0; i < c_vs.length; i++) {
            ArithVar v = c_vs[i];
            if (vars.get(v) > 0) {
                if (vars.get(v) == 1) {
                    if (i == 0) {
                        lin += v.name;
                    } else {
                        lin += " + " + v.name;
                    }
                } else {
                    lin += " + " + vars.get(v) + v.name;
                }
            }
            if (vars.get(v) < 0) {
                if (vars.get(v) == -1) {
                    if (i == 0) {
                        lin += "-" + v.name;
                    } else {
                        lin += " - " + v.name;
                    }
                } else {
                    lin += " - " + -vars.get(v) + v.name;
                }
            }
        }
        if (known_term > 0) {
            lin += " + " + known_term;
        }
        if (known_term < 0) {
            lin += " - " + -known_term;
        }
        return lin;
    }

    @Override
    public Interval evaluate() {
        Interval res = new Interval(known_term);
        for (Map.Entry<ArithVar, Double> entry : vars.entrySet()) {
            res.add(new Interval(entry.getKey().domain).multiply(entry.getValue()));
        }
        return res;
    }

    @Override
    public Var<Interval> to_var(Network n) {
        String id = id();
        if (n.arith_vars.containsKey(id)) {
            // we can recycle an existing var..
            return n.arith_vars.get(id);
        } else {
            // we need to create a new variable..
            ArithVar sum;
            if (n.rootLevel()) {
                assert !evaluate().isSingleton();
                sum = new ArithVar(n, "s" + n.n_slack_vars++, evaluate());
            } else {
                sum = new ArithVar(n, "s" + n.n_slack_vars++);
                sum.intersect(evaluate(), null);
            }
            sum.val = n.evaluate(this);
            assert sum.val >= evaluate().lb && sum.val <= evaluate().ub;
            n.arith_vars.put(sum.name, sum);
            n.store(new Propagator() {
                @Override
                public Var<?>[] getArgs() {
                    ArrayList<Var<?>> args = new ArrayList<>(vars.size() + 1);
                    args.addAll(vars.keySet());
                    args.add(sum);
                    return args.toArray(new Var<?>[args.size()]);
                }

                @Override
                public boolean propagate(Var<?> v) {
                    Interval eval = evaluate();
                    if (v != sum) {
                        // we update the current bounds..
                        if (!sum.intersect(new Interval(eval), this)) {
                            return false;
                        }
                    }
                    for (Map.Entry<ArithVar, Double> v0 : vars.entrySet()) {
                        if (v != v0.getKey()) {
                            // we update other variables bounds..
                            Interval c_bound = new Interval(eval);
                            c_bound.subtract(known_term);
                            for (Map.Entry<ArithVar, Double> v1 : vars.entrySet()) {
                                if (v0.getKey() != v1.getKey()) {
                                    c_bound.subtract(Interval.multiply(v1.getKey().domain, v1.getValue()));
                                }
                            }
                            if (!v0.getKey().intersect(c_bound.divide(v0.getValue()), this)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }

                @Override
                public String toString() {
                    return sum.name;
                }
            });
            n.tableau.put(sum, new Lin(this));
            return sum;
        }
    }

    public static Lin negate(Lin lhs) {
        Lin res = new Lin(lhs);
        for (Map.Entry<ArithVar, Double> entry : res.vars.entrySet()) {
            entry.setValue(-entry.getValue());
        }
        res.known_term = -res.known_term;
        return res;
    }

    public static Lin multiply(Lin lhs, double rhs) {
        Lin res = new Lin(lhs);
        for (Map.Entry<ArithVar, Double> entry : res.vars.entrySet()) {
            entry.setValue(entry.getValue() * rhs);
        }
        res.known_term *= rhs;
        for (Iterator<Map.Entry<ArithVar, Double>> it = res.vars.entrySet().iterator(); it.hasNext();) {
            Map.Entry<ArithVar, Double> next = it.next();
            if (next.getValue() == 0) {
                it.remove();
            }
        }
        return res;
    }

    public static Lin divide(Lin lhs, double rhs) {
        Lin res = new Lin(lhs);
        for (Map.Entry<ArithVar, Double> entry : res.vars.entrySet()) {
            entry.setValue(entry.getValue() / rhs);
        }
        res.known_term /= rhs;
        for (Iterator<Map.Entry<ArithVar, Double>> it = res.vars.entrySet().iterator(); it.hasNext();) {
            Map.Entry<ArithVar, Double> next = it.next();
            if (next.getValue() == 0) {
                it.remove();
            }
        }
        return res;
    }

    public Lin negate() {
        for (Map.Entry<ArithVar, Double> entry : vars.entrySet()) {
            entry.setValue(-entry.getValue());
        }
        known_term = -known_term;
        return this;
    }

    public Lin add(Lin rhs) {
        for (Map.Entry<ArithVar, Double> entry : rhs.vars.entrySet()) {
            vars.put(entry.getKey(), vars.containsKey(entry.getKey()) ? vars.get(entry.getKey()) + entry.getValue() : entry.getValue());
            if (vars.get(entry.getKey()) == 0) {
                vars.remove(entry.getKey());
            }
        }
        known_term += rhs.known_term;
        return this;
    }

    public Lin add(ArithVar var) {
        vars.put(var, 1.0);
        return this;
    }

    public Lin add(double rhs) {
        known_term += rhs;
        return this;
    }

    public Lin subtract(Lin rhs) {
        for (Map.Entry<ArithVar, Double> entry : rhs.vars.entrySet()) {
            vars.put(entry.getKey(), vars.containsKey(entry.getKey()) ? vars.get(entry.getKey()) - entry.getValue() : -entry.getValue());
            if (vars.get(entry.getKey()) == 0) {
                vars.remove(entry.getKey());
            }
        }
        known_term -= rhs.known_term;
        return this;
    }

    public Lin subtract(ArithVar var) {
        vars.put(var, -1.0);
        return this;
    }

    public Lin subtract(double rhs) {
        known_term -= rhs;
        return this;
    }

    public Lin multiply(double rhs) {
        for (Map.Entry<ArithVar, Double> entry : vars.entrySet()) {
            entry.setValue(entry.getValue() * rhs);
        }
        known_term *= rhs;
        for (Iterator<Map.Entry<ArithVar, Double>> it = vars.entrySet().iterator(); it.hasNext();) {
            Map.Entry<ArithVar, Double> next = it.next();
            if (next.getValue() == 0) {
                it.remove();
            }
        }
        return this;
    }

    public Lin divide(double rhs) {
        for (Map.Entry<ArithVar, Double> entry : vars.entrySet()) {
            entry.setValue(entry.getValue() / rhs);
        }
        known_term /= rhs;
        for (Iterator<Map.Entry<ArithVar, Double>> it = vars.entrySet().iterator(); it.hasNext();) {
            Map.Entry<ArithVar, Double> next = it.next();
            if (next.getValue() == 0) {
                it.remove();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return id();
    }
}
