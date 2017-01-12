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
import it.cnr.istc.oratio.core.gui.EnvTreeCellRenderer;
import it.cnr.istc.oratio.core.gui.EnvTreeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class AtomFlaw extends Flaw {

    private final Atom atom;
    private final boolean fact;

    AtomFlaw(Solver s, Resolver c, Atom a, boolean fact) {
        super(s, c);
        this.atom = a;
        this.fact = fact;
    }

    @Override
    protected void computeResolvers(Collection<Resolver> rs) {
        for (IItem inst : atom.type.getInstances()) {
            Atom a = (Atom) inst;
            if (atom != a && solver.reasons.get(a).isSolved() && atom.state.evaluate().contains(AtomState.Unified) && a.state.evaluate().contains(AtomState.Active) && atom.equates(a)) {
                // this atom is a good candidate for unification
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
                f = this;
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
                and.add(solver.network.eq(atom.state, AtomState.Unified));
                and.add(solver.network.eq(a.state, AtomState.Active));
                and.add(atom.eq(a));
                BoolExpr eq = solver.network.and(and.toArray(new BoolExpr[and.size()]));
                if (solver.check(eq)) {
                    // unification is actually possible!
                    UnifyGoal unify = new UnifyGoal(solver, solver.network.newReal(0), this, atom, a, eq);
                    unify.fireNewResolver();
                    rs.add(unify);
                    updateCosts(new HashSet<>());
                    boolean add_pre = unify.addPrecondition(solver.reasons.get(a));
                    assert add_pre;
                }
            }
        }

        if (fact) {
            AddFact af = new AddFact(solver, solver.network.newReal(0), this, atom);
            af.fireNewResolver();
            rs.add(af);
        } else {
            if (rs.isEmpty()) {
                // we remove unification from atom state..
                boolean not_unify = solver.network.add(solver.network.not(solver.network.eq(atom.state, AtomState.Unified))) && solver.network.propagate();
                assert not_unify;
            }
            ExpandGoal eg = new ExpandGoal(solver, solver.network.newReal(1), this, atom);
            eg.fireNewResolver();
            rs.add(eg);
        }
    }

    @Override
    public JComponent getDetails() {
        JTree tree = new JTree();
        EnvTreeModel model = new EnvTreeModel();
        model.setEnv(atom);
        tree.setModel(model);
        tree.setCellRenderer(new EnvTreeCellRenderer());
        tree.setRootVisible(false);
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                model.createChilds((DefaultMutableTreeNode) event.getPath().getLastPathComponent());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            }
        });
        return tree;
    }

    @Override
    public String toSimpleString() {
        return (fact ? "fact " : "goal ") + atom.type.name;
    }

    private static class AddFact extends Resolver {

        private final Atom atom;

        AddFact(Solver s, ArithExpr c, Flaw e, Atom atom) {
            super(s, c, e);
            this.atom = atom;
        }

        @Override
        protected boolean apply() {
            return solver.activateFact(atom) && solver.network.add(solver.network.imply(in_plan, solver.network.eq(((AtomFlaw) effect).atom.state, AtomState.Active))) && solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return "add fact";
        }
    }

    private static class ExpandGoal extends Resolver {

        private final Atom atom;

        ExpandGoal(Solver s, ArithExpr c, Flaw e, Atom atom) {
            super(s, c, e);
            this.atom = atom;
        }

        @Override
        protected boolean apply() {
            return solver.activateGoal(atom) && solver.network.add(solver.network.imply(in_plan, solver.network.eq(((AtomFlaw) effect).atom.state, AtomState.Active))) && ((Predicate) ((AtomFlaw) effect).atom.type).apply(((AtomFlaw) effect).atom) && solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return "expand";
        }
    }

    private static class UnifyGoal extends Resolver {

        private final Atom unifying;
        private final Atom with;
        private final BoolExpr eq_expr;

        UnifyGoal(Solver s, ArithExpr c, Flaw e, Atom unifying, Atom with, BoolExpr eq_expr) {
            super(s, c, e);
            this.unifying = unifying;
            this.with = with;
            this.eq_expr = eq_expr;
            estimated_cost = 0;
        }

        @Override
        protected boolean apply() {
            return (((AtomFlaw) effect).fact ? solver.unifyFact(unifying, with) : solver.unifyGoal(unifying, with)) && solver.network.add(solver.network.imply(in_plan, eq_expr)) && solver.network.propagate();
        }

        @Override
        public String toSimpleString() {
            return "unify";
        }
    }
}
