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
import it.cnr.istc.ac.Propagator;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
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
                return true;
            }
        });
    }

    @Override
    protected void predicateDefined(Predicate predicate) {
        throw new AssertionError("reusable resource predicates are predefined..");
    }

    @Override
    protected boolean factCreated(Atom atom) {
        if (super.factCreated(atom)) {
            core.network.store(new AtomPropagator(atom));
            return core.getPredicate("IntervalPredicate").apply(atom);
        } else {
            return false;
        }
    }

    @Override
    protected boolean goalCreated(Atom atom) {
        throw new AssertionError("it is not possible to create goals on reusable resources..");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class AtomPropagator implements Propagator {

        private final Atom atom;

        AtomPropagator(Atom atom) {
            this.atom = atom;
        }

        @Override
        public Var<?>[] getArgs() {
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
        public boolean propagate(Var<?> v) {
            for (IItem i : ((IEnumItem) atom.get(SCOPE)).getEnumVar().evaluate().getAllowedValues()) {
                to_check.add(i);
            }
            return true;
        }
    }

    private static class ReusableResourceFlaw extends Flaw {

        private final Collection<BoolExpr> or;

        ReusableResourceFlaw(Solver s, Resolver c, Collection<BoolExpr> or) {
            super(s, c);
            this.or = or;
        }

        @Override
        protected boolean computeResolvers(Collection<Resolver> rs) {
            for (BoolExpr expr : or) {
                ReusableResourceResolver rrr = new ReusableResourceResolver(solver, solver.network.newReal(1.0 / or.size()), this, expr);
                rrr.fireNewResolver();
                rs.add(rrr);
            }

            return true;
        }

        @Override
        public String toSimpleString() {
            return "rr-flaw";
        }

        @Override
        public String toString() {
            return or.stream().map(v -> v.toString()).collect(Collectors.joining(" | "));
        }
    }

    private static class ReusableResourceResolver extends Resolver {

        private final BoolExpr expr;

        ReusableResourceResolver(Solver s, ArithExpr c, Flaw e, BoolExpr expr) {
            super(s, c, e);
            this.expr = expr;
        }

        @Override
        protected void fireNewResolver() {
            super.fireNewResolver();
        }

        @Override
        protected boolean apply() {
            return solver.network.add(solver.network.imply(in_plan, expr)) && solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return expr.id();
        }
    }
}
