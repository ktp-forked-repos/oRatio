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
package it.cnr.istc.oratio.solver;

import it.cnr.istc.ac.ArithExpr;
import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.AtomState;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class GFlaw extends Flaw {

    private final Atom atom;

    GFlaw(Solver s, Resolver c, Atom a) {
        super(s, c);
        this.atom = a;
    }

    @Override
    protected boolean computeResolvers(Collection<Resolver> rs) {
        boolean solved = false;
        for (IItem inst : atom.type.getInstances()) {
            Atom a = (Atom) inst;
            if (atom != a && atom.state.evaluate().contains(AtomState.Unified) && a.state.evaluate().contains(AtomState.Active) && atom.equates(a)) {
                // this atom is a good candidate for unification
                UnifyGoal unify = new UnifyGoal(solver, solver.network.newReal(0), this, a);
                solver.network.push();
                solver.network.add(unify.in_plan);
                if (solver.network.propagate()) {
                    // unification is actually possible!
                    solved = true;
                    estimated_cost = 0;
                    updateCosts(new HashSet<>());
                    rs.add(unify);
                    boolean add_pre = unify.addPrecondition(solver.reasons.get(a));
                    assert add_pre;
                }
                solver.network.pop();
            }
        }

        rs.add(new ExpandGoal(solver, solver.network.newReal(1), this));

        if (!solved) {
            // we remove unification from atom state..
            solver.network.add(solver.network.not(solver.network.eq(atom.state, AtomState.Unified)));
            boolean propagate = solver.network.propagate();
            assert propagate;
        }

        return solved;
    }

    @Override
    public String toSimpleString() {
        return "goal " + atom.type.name;
    }

    private static class ExpandGoal extends Resolver {

        ExpandGoal(Solver s, ArithExpr c, Flaw e) {
            super(s, c, e);
        }

        @Override
        protected boolean apply() {
            solver.network.add(solver.network.imply(in_plan, solver.network.eq(((GFlaw) effect).atom.state, AtomState.Active)));
            return ((Predicate) ((GFlaw) effect).atom.type).apply(((GFlaw) effect).atom) && solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return "expand";
        }
    }

    private static class UnifyGoal extends Resolver {

        private final Atom atom;
        private final BoolExpr eq_expr;

        UnifyGoal(Solver s, ArithExpr c, Flaw e, Atom atom) {
            super(s, c, e);
            this.atom = atom;
            Collection<BoolExpr> and = new ArrayList<>();
            Flaw f = solver.reasons.get(atom);
            while (f != null) {
                assert f.in_plan.evaluate() != LBool.L_FALSE;
                assert f.cause.in_plan.evaluate() != LBool.L_FALSE;
                if (!f.in_plan.isSingleton()) {
                    and.add(f.in_plan);
                }
                if (!f.cause.in_plan.isSingleton()) {
                    and.add(f.cause.in_plan);
                }
                f = f.cause.effect;
            }
            f = e;
            while (f != null) {
                assert f.in_plan.evaluate() != LBool.L_FALSE;
                assert f.cause.in_plan.evaluate() != LBool.L_FALSE;
                if (!f.in_plan.isSingleton()) {
                    and.add(f.in_plan);
                }
                if (!f.cause.in_plan.isSingleton()) {
                    and.add(f.cause.in_plan);
                }
                f = f.cause.effect;
            }
            this.eq_expr = s.network.and(and.toArray(new BoolExpr[and.size()]));
        }

        @Override
        protected boolean apply() {
            estimated_cost = 0;
            solver.network.add(
                    solver.network.imply(in_plan, solver.network.eq(((GFlaw) effect).atom.state, AtomState.Unified)),
                    eq_expr
            );
            return solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return "unify";
        }
    }
}
