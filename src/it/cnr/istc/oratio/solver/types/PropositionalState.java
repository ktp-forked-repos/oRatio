/*
 * Copyright (C) 2017 Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
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
package it.cnr.istc.oratio.solver.types;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.DomainListener;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.ac.Var;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.AtomState;
import it.cnr.istc.oratio.core.Constructor;
import static it.cnr.istc.oratio.core.Core.BOOL;
import it.cnr.istc.oratio.core.Field;
import it.cnr.istc.oratio.core.IArithItem;
import it.cnr.istc.oratio.core.IBoolItem;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Predicate;
import it.cnr.istc.oratio.core.Type;
import it.cnr.istc.oratio.solver.Flaw;
import it.cnr.istc.oratio.solver.Resolver;
import it.cnr.istc.oratio.solver.SmartType;
import it.cnr.istc.oratio.solver.Solver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class PropositionalState extends SmartType {

    public static final String NAME = "PropositionalState";
    public static final String POLARITY = "polarity";
    private final Collection<Predicate> defined_predicates = new ArrayList<>();
    private final Set<Atom> to_check = new HashSet<>();

    public PropositionalState(Solver s) {
        super(s, s, NAME);
        constructors.add(new Constructor(core, this) {
            @Override
            public boolean invoke(IItem item, IItem... expressions) {
                return true;
            }
        });
        predicates.put("PropositionalStatePredicate", new Predicate(core, this, "PropositionalStatePredicate", new Field(core.getType(BOOL), POLARITY)) {
            @Override
            public boolean apply(Atom atom) {
                return true;
            }
        });
    }

    @Override
    protected void predicateDefined(Predicate predicate) {
        if (!defined_predicates.contains(predicate)) {
            extendPredicate(predicate, core.getPredicate("IntervalPredicate"));
            extendPredicate(predicate, predicates.get("PropositionalStatePredicate"));
            defined_predicates.add(predicate);
        }
    }

    @Override
    protected boolean factActivated(Atom atom) {
        if (super.factActivated(atom)) {
            core.network.addDomainListener(new AtomListener(atom));
            to_check.add(atom);
            return core.getPredicate("IntervalPredicate").apply(atom) && predicates.get("PropositionalStatePredicate").apply(atom);
        } else {
            return false;
        }
    }

    @Override
    protected boolean goalActivated(Atom atom) {
        if (super.goalActivated(atom)) {
            core.network.addDomainListener(new AtomListener(atom));
            to_check.add(atom);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<Flaw> getInconsistencies() {
        if (to_check.isEmpty()) {
            // nothing has changed since last inconsistency check..
            return Collections.emptyList();
        }

        Collection<Flaw> fs = new ArrayList<>();
        Map<Type, List<Atom>> c_atoms = to_check.stream().filter(a -> a.state.evaluate().isSingleton() && a.state.evaluate().contains(AtomState.Active)).collect(Collectors.groupingBy(Atom::getType));
        for (Type type : c_atoms.keySet()) {
            Collection<Field> c_fields = new ArrayList<>();
            Set<Type> c_types = new HashSet<>();
            LinkedList<Type> queue = new LinkedList<>();
            queue.add(type);
            while (!queue.isEmpty()) {
                Type c_type = queue.pollFirst();
                if (!c_types.contains(c_type)) {
                    c_types.add(c_type);
                    queue.addAll(c_type.getSuperclasses());
                }
            }

            for (Type t : c_types) {
                for (Field f : t.getFields()) {
                    if (!f.synthetic && !(f.name.equals(POLARITY) || f.name.equals("start") || f.name.equals("end") || f.name.equals("duration"))) {
                        c_fields.add(f);
                    }
                }
            }

            List<Atom> atoms = c_atoms.get(type);
            for (Atom a0 : atoms) {
                for (Atom a1 : atoms) {
                    if (a0 != a1) {
                        LBool a0_polarity = ((IBoolItem) a0.get(POLARITY)).getBoolVar().evaluate();
                        LBool a1_polarity = ((IBoolItem) a1.get(POLARITY)).getBoolVar().evaluate();
                        assert a0_polarity.isSingleton();
                        assert a1_polarity.isSingleton();
                        if (a0_polarity != a1_polarity) {
                            // the polarity is different.. we might need to generate a flaw..
                            if (c_fields.stream().allMatch(f -> a0.get(f.name).equates(a1.get(f.name)))) {
                                // the two atoms are on the same propositional predicate..
                                ArithExpr a0_start = ((IArithItem) a0.get("start")).getArithVar();
                                ArithExpr a0_end = ((IArithItem) a0.get("end")).getArithVar();

                                ArithExpr a1_start = ((IArithItem) a1.get("start")).getArithVar();
                                ArithExpr a1_end = ((IArithItem) a1.get("end")).getArithVar();

                                if (core.network.evaluate(a0_end) > core.network.evaluate(a1_start) && core.network.evaluate(a0_start) < core.network.evaluate(a1_end)) {
                                    // they also overlap..
                                    Collection<BoolExpr> or = new ArrayList<>();

                                    // either we order..
                                    BoolExpr a0_before_a1 = core.network.leq(a0_end, a1_start);
                                    if (a0_before_a1.root() != LBool.L_FALSE) {
                                        or.add(a0_before_a1);
                                    }
                                    BoolExpr a1_before_a0 = core.network.leq(a1_end, a0_start);
                                    if (a1_before_a0.root() != LBool.L_FALSE) {
                                        or.add(a1_before_a0);
                                    }

                                    // or we force on another propositional predicate..
                                    for (Field f : c_fields) {
                                        IItem a0_arg = a0.get(f.name);
                                        if (a0_arg instanceof IEnumItem) {
                                            Set<IItem> a0_arg_vals = ((IEnumItem) a0_arg).getEnumVar().root().getAllowedValues();
                                            if (a0_arg_vals.size() > 1) {
                                                for (IItem a0_arg_val : a0_arg_vals) {
                                                    or.add(core.network.not(((IEnumItem) a0_arg).allows(a0_arg_val)));
                                                }
                                            }
                                        }

                                        IItem a1_arg = a1.get(f.name);
                                        if (a1_arg instanceof IEnumItem) {
                                            Set<IItem> a1_arg_vals = ((IEnumItem) a1_arg).getEnumVar().root().getAllowedValues();
                                            if (a1_arg_vals.size() > 1) {
                                                for (IItem a1_arg_val : a1_arg_vals) {
                                                    or.add(core.network.not(((IEnumItem) a1_arg).allows(a1_arg_val)));
                                                }
                                            }
                                        }
                                    }

                                    fs.add(new PropositionalStateFlaw((Solver) core, or));
                                }
                            }
                        }
                    }
                }
            }
        }

        to_check.clear();
        return fs;
    }

    private class AtomListener implements DomainListener {

        private final Atom atom;

        AtomListener(Atom atom) {
            this.atom = atom;
        }

        @Override
        public Var<?>[] getVars() {
            return atom.getItems().values().stream().filter(i -> (i instanceof IBoolItem || i instanceof IArithItem || i instanceof IEnumItem)).map(i -> {
                if (i instanceof IBoolItem) {
                    return ((IBoolItem) i).getBoolVar().to_var(core.network);
                } else if (i instanceof IArithItem) {
                    return ((IArithItem) i).getArithVar().to_var(core.network);
                } else {
                    return ((IEnumItem) i).getEnumVar().to_var(core.network);
                }
            }).toArray(Var<?>[]::new);
        }

        @Override
        public void domainChange(Var<?> var) {
            to_check.add(atom);
        }
    }

    private static class PropositionalStateFlaw extends Flaw {

        private final Collection<BoolExpr> or;

        PropositionalStateFlaw(Solver s, Collection<BoolExpr> or) {
            super(s, false);
            this.or = or;
        }

        @Override
        protected void computeResolvers(Collection<Resolver> rs) {
            for (BoolExpr expr : or) {
                rs.add(new PropositionalStateResolver(solver, solver.network.newReal(1.0 / or.size()), this, expr));
            }
        }

        @Override
        public String toSimpleString() {
            return "ps-flaw";
        }

        @Override
        public String toString() {
            return or.stream().map(v -> v.toString()).collect(Collectors.joining(" | ")) + " " + in_plan.evaluate() + " " + solver.getCost(this);
        }
    }

    private static class PropositionalStateResolver extends Resolver {

        private final BoolExpr expr;

        PropositionalStateResolver(Solver s, ArithExpr c, Flaw e, BoolExpr expr) {
            super(s, c, e);
            this.expr = expr;
        }

        @Override
        protected boolean apply() {
            return solver.network.add(solver.network.imply(in_plan, expr));
        }

        @Override
        public String toSimpleString() {
            return expr.id();
        }
    }
}
