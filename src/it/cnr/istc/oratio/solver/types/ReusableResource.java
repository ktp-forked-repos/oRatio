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
package it.cnr.istc.oratio.solver.types;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.DomainListener;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.ac.Var;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.AtomState;
import it.cnr.istc.oratio.core.Constructor;
import static it.cnr.istc.oratio.core.Core.REAL;
import it.cnr.istc.oratio.core.Field;
import it.cnr.istc.oratio.core.IArithItem;
import it.cnr.istc.oratio.core.IBoolItem;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IItem;
import static it.cnr.istc.oratio.core.IScope.SCOPE;
import it.cnr.istc.oratio.core.Predicate;
import it.cnr.istc.oratio.solver.Flaw;
import it.cnr.istc.oratio.solver.Resolver;
import it.cnr.istc.oratio.solver.SmartType;
import it.cnr.istc.oratio.solver.Solver;
import it.cnr.istc.utils.CombinationGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ReusableResource extends SmartType {

    public static final String NAME = "ReusableResource";
    public static final String CAPACITY = "capacity";
    public static final String USE_PREDICATE_NAME = "Use";
    public static final String AMOUNT = "amount";
    private final Set<IItem> to_check = new HashSet<>();

    public ReusableResource(Solver s) {
        super(s, s, NAME);
        fields.put(CAPACITY, new Field(s.getType(REAL), CAPACITY));
        constructors.add(new Constructor(core, this) {
            @Override
            public boolean invoke(IItem item, IItem... expressions) {
                assert expressions.length == 0;
                IArithItem capacity = core.newReal();
                set(item, scope.getField(CAPACITY), capacity);
                return core.add(core.geq(capacity, core.newReal(0)));
            }
        });
        constructors.add(new Constructor(core, this, new Field(core.getType(REAL), CAPACITY)) {
            @Override
            public boolean invoke(IItem item, IItem... expressions) {
                assert expressions.length == 1;
                IArithItem capacity = (IArithItem) expressions[0];
                set(item, scope.getField(CAPACITY), capacity);
                return core.add(core.geq(capacity, core.newReal(0)));
            }
        });
        predicates.put(USE_PREDICATE_NAME, new Predicate(core, this, USE_PREDICATE_NAME, new Field(core.getType(REAL), AMOUNT)) {
            @Override
            public boolean apply(Atom atom) {
                throw new AssertionError("this rule should never be applied..");
            }
        });
        extendPredicate(predicates.get(USE_PREDICATE_NAME), core.getPredicate("IntervalPredicate"));
    }

    @Override
    protected void predicateDefined(Predicate predicate) {
        throw new AssertionError("reusable resource predicates are predefined..");
    }

    @Override
    protected boolean factCreated(Atom atom) {
        if (super.factCreated(atom)) {
            return core.network.add(core.network.not(core.network.eq(atom.state, AtomState.Unified)));
        } else {
            return false;
        }
    }

    @Override
    protected boolean factActivated(Atom atom) {
        if (super.factActivated(atom)) {
            core.network.addDomainListener(new AtomListener(atom));
            to_check.addAll(((IEnumItem) atom.get(SCOPE)).getEnumVar().evaluate().getAllowedValues());
            return core.getPredicate("IntervalPredicate").apply(atom);
        } else {
            return false;
        }
    }

    @Override
    protected boolean factUnified(Atom unifying, Atom with) {
        throw new AssertionError("facts cannot unify on reusable resources..");
    }

    @Override
    protected boolean goalCreated(Atom atom) {
        throw new AssertionError("goals cannot be created on reusable resources..");
    }

    @Override
    protected boolean goalActivated(Atom atom) {
        throw new AssertionError("goals cannot be activated on reusable resources..");
    }

    @Override
    protected boolean goalUnified(Atom unifying, Atom with) {
        throw new AssertionError("goals cannot be unified on reusable resources..");
    }

    @Override
    public Collection<Flaw> getInconsistencies() {
        if (to_check.isEmpty()) {
            // nothing has changed since last inconsistency check..
            return Collections.emptyList();
        }

        Map<IItem, Collection<Atom>> instances = new IdentityHashMap<>(to_check.size());
        for (IItem i : to_check) {
            instances.put(i, new ArrayList<>());
        }
        for (Atom atom : predicates.values().stream().flatMap(p -> p.getInstances().stream()).map(a -> (Atom) a).filter(a -> a.state.evaluate().isSingleton() && a.state.evaluate().contains(AtomState.Active)).collect(Collectors.toList())) {
            for (IItem i : ((IEnumItem) atom.get(SCOPE)).getEnumVar().evaluate().getAllowedValues()) {
                if (instances.containsKey(i)) {
                    instances.get(i).add(atom);
                }
            }
        }

        Collection<Flaw> fs = new ArrayList<>();
        for (IItem i : to_check) {
            Collection<Atom> atoms = instances.get(i);

            // For each pulse the atoms starting at that pulse
            Map<Double, Collection<Atom>> starting_atoms = new HashMap<>(atoms.size());
            // For each pulse the atoms ending at that pulse
            Map<Double, Collection<Atom>> ending_atoms = new HashMap<>(atoms.size());

            // The pulses of the timeline
            Set<Double> c_pulses = new HashSet<>(atoms.size() * 2);

            for (Atom atom : atoms) {
                double start = core.network.evaluate(((IArithItem) atom.get("start")).getArithVar());
                double end = core.network.evaluate(((IArithItem) atom.get("end")).getArithVar());

                if (!starting_atoms.containsKey(start)) {
                    starting_atoms.put(start, new ArrayList<>());
                }
                starting_atoms.get(start).add(atom);

                if (!ending_atoms.containsKey(end)) {
                    ending_atoms.put(end, new ArrayList<>());
                }
                ending_atoms.get(end).add(atom);

                c_pulses.add(start);
                c_pulses.add(end);
            }

            // we sort current pulses..
            Double[] c_pulses_array = c_pulses.toArray(new Double[c_pulses.size()]);
            Arrays.sort(c_pulses_array);

            List<Atom> overlapping_atoms = new ArrayList<>();
            for (Double p : c_pulses_array) {
                if (starting_atoms.containsKey(p)) {
                    overlapping_atoms.addAll(starting_atoms.get(p));
                }
                if (ending_atoms.containsKey(p)) {
                    overlapping_atoms.removeAll(ending_atoms.get(p));
                }
                if (overlapping_atoms.size() > 1 && core.network.evaluate(core.network.sum(overlapping_atoms.stream().map(atom -> ((IArithItem) atom.get(AMOUNT)).getArithVar()).toArray(ArithExpr[]::new))) > core.network.evaluate(((IArithItem) i.get(CAPACITY)).getArithVar())) {
                    Collection<BoolExpr> or = new ArrayList<>();
                    for (Atom[] as : new CombinationGenerator<>(2, overlapping_atoms.toArray(new Atom[overlapping_atoms.size()]))) {
                        ArithExpr a0_start = ((IArithItem) as[0].get("start")).getArithVar();
                        ArithExpr a0_end = ((IArithItem) as[0].get("end")).getArithVar();

                        ArithExpr a1_start = ((IArithItem) as[1].get("start")).getArithVar();
                        ArithExpr a1_end = ((IArithItem) as[1].get("end")).getArithVar();

                        BoolExpr a0_before_a1 = core.network.leq(a0_end, a1_start);
                        if (a0_before_a1.evaluate() != LBool.L_FALSE) {
                            or.add(a0_before_a1);
                        }
                        BoolExpr a1_before_a0 = core.network.leq(a1_end, a0_start);
                        if (a1_before_a0.evaluate() != LBool.L_FALSE) {
                            or.add(a1_before_a0);
                        }

                        IEnumItem a0_scope = (IEnumItem) as[0].get(SCOPE);
                        Set<IItem> a0_scopes = a0_scope.getEnumVar().evaluate().getAllowedValues();
                        if (a0_scopes.size() > 1) {
                            for (IItem a0_s : a0_scopes) {
                                or.add(core.network.not(a0_scope.allows(a0_s)));
                            }
                        }

                        IEnumItem a1_scope = (IEnumItem) as[1].get(SCOPE);
                        Set<IItem> a1_scopes = a1_scope.getEnumVar().evaluate().getAllowedValues();
                        if (a1_scopes.size() > 1) {
                            for (IItem a1_s : a1_scopes) {
                                or.add(core.network.not(a1_scope.allows(a1_s)));
                            }
                        }
                    }
                    if (or.isEmpty()) {
                        System.out.println("prc..");
                    }

                    fs.add(new ReusableResourceFlaw((Solver) core, or));
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
            for (IItem i : ((IEnumItem) atom.get(SCOPE)).getEnumVar().evaluate().getAllowedValues()) {
                to_check.add(i);
            }
        }
    }

    private static class ReusableResourceFlaw extends Flaw {

        private final Collection<BoolExpr> or;

        ReusableResourceFlaw(Solver s, Collection<BoolExpr> or) {
            super(s, false);
            this.or = or;
        }

        @Override
        protected void computeResolvers(Collection<Resolver> rs) {
            for (BoolExpr expr : or) {
                rs.add(new ReusableResourceResolver(solver, solver.network.newReal(1.0 / or.size()), this, expr));
            }
        }

        @Override
        public String toSimpleString() {
            return "rr-flaw";
        }

        @Override
        public String toString() {
            return or.stream().map(v -> v.toString()).collect(Collectors.joining(" | ")) + " " + in_plan.evaluate() + " " + solver.getCost(this);
        }
    }

    private static class ReusableResourceResolver extends Resolver {

        private final BoolExpr expr;

        ReusableResourceResolver(Solver s, ArithExpr c, Flaw e, BoolExpr expr) {
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
