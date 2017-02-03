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
package it.cnr.istc.iloc;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.InconsistencyException;
import it.cnr.istc.core.Atom;
import it.cnr.istc.core.AtomState;
import it.cnr.istc.core.IItem;
import it.cnr.istc.core.Predicate;
import java.util.Collection;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class AtomFlaw extends Flaw {

    private final Atom atom;
    private final boolean fact;

    AtomFlaw(Solver s, Atom a, boolean fact) {
        super(s, true);
        this.atom = a;
        this.fact = fact;
    }

    @Override
    protected boolean computeResolvers(Collection<Resolver> rs) {
        for (IItem inst : atom.type.getInstances()) {
            Atom a = (Atom) inst;
            if (atom != a && atom.state.evaluate().contains(AtomState.Unified) && a.state.isSingleton() && a.state.evaluate().contains(AtomState.Active) && atom.equates(a)) {
                BoolExpr eq = solver.and(
                        solver.eq(atom.state, AtomState.Unified),
                        solver.eq(a.state, AtomState.Active),
                        atom.eq(a)
                );
                try {
                    if (solver.check(eq)) {
                        // unification is actually possible!
                        UnifyGoal unify = new UnifyGoal(solver, this, atom, a, eq);
                        rs.add(unify);
                        if (fact) {
                            solver.unifyFact(atom, a);
                        } else {
                            solver.unifyGoal(atom, a);
                        }
                    }
                } catch (InconsistencyException ex) {
                    return false;
                }
            }
        }

        if (rs.isEmpty()) {
            // we remove unification from atom state..
            boolean not_unify = solver.add(solver.not(solver.eq(atom.state, AtomState.Unified)));
            assert not_unify;
        }
        if (fact) {
            rs.add(new AddFact(solver, this, atom));
            solver.activateFact(atom);
        } else {
            rs.add(new ExpandGoal(solver, this, atom));
            solver.activateGoal(atom);
        }

        return true;
    }

    private static class AddFact extends Resolver {

        private final Atom atom;

        AddFact(Solver s, Flaw e, Atom atom) {
            super(s, s.newReal(0), e);
            this.atom = atom;
        }

        @Override
        protected boolean apply() {
            return solver.add(solver.imply(in_plan, solver.eq(((AtomFlaw) effect).atom.state, AtomState.Active)));
        }

        @Override
        public String toSimpleString() {
            return "expand fact " + atom.type.name;
        }
    }

    private static class ExpandGoal extends Resolver {

        private final Atom atom;

        ExpandGoal(Solver s, Flaw e, Atom atom) {
            super(s, s.newReal(1), e);
            this.atom = atom;
        }

        @Override
        protected boolean apply() {
            return solver.add(solver.imply(in_plan, solver.eq(((AtomFlaw) effect).atom.state, AtomState.Active))) && ((Predicate) ((AtomFlaw) effect).atom.type).apply(((AtomFlaw) effect).atom);
        }

        @Override
        public String toSimpleString() {
            return "expand goal " + atom.type.name;
        }
    }

    private static class UnifyGoal extends Resolver {

        private final Atom unifying;
        private final Atom with;
        private final BoolExpr eq_expr;

        UnifyGoal(Solver s, Flaw e, Atom unifying, Atom with, BoolExpr eq_expr) {
            super(s, s.newReal(0), e);
            this.unifying = unifying;
            this.with = with;
            this.eq_expr = eq_expr;
        }

        @Override
        protected boolean apply() {
            return solver.add(solver.imply(in_plan, eq_expr));
        }

        @Override
        public String toSimpleString() {
            return "unify " + unifying.type.name;
        }
    }
}
