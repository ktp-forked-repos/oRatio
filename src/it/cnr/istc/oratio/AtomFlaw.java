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
package it.cnr.istc.oratio;

import it.cnr.istc.ac.BoolExpr;
import it.cnr.istc.ac.LBool;
import it.cnr.istc.core.Atom;
import it.cnr.istc.core.AtomState;
import it.cnr.istc.core.IItem;
import it.cnr.istc.core.InconsistencyException;
import it.cnr.istc.core.Predicate;
import it.cnr.istc.core.gui.EnvTreeCellRenderer;
import it.cnr.istc.core.gui.EnvTreeModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
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

    AtomFlaw(Solver s, Atom a, boolean fact) {
        super(s, true);
        this.atom = a;
        this.fact = fact;
    }

    @Override
    protected boolean computeResolvers(Collection<Resolver> rs) {
        atoms:
        for (IItem inst : atom.type.getInstances()) {
            Atom a = (Atom) inst;
            if (atom != a && solver.reasons.get(a).isExpanded() && (!solver.rootLevel() || (atom.state.evaluate().contains(AtomState.Unified) && a.state.evaluate().contains(AtomState.Active) && atom.equates(a)))) {
                // this atom is a good candidate for unification
                Collection<BoolExpr> and = new ArrayList<>();
                LinkedList<Flaw> queue = new LinkedList<>();
                queue.add(this);
                queue.add(solver.reasons.get(a));
                while (!queue.isEmpty()) {
                    Flaw f = queue.pollFirst();
                    if (f.in_plan.isConst()) {
                        if (f.in_plan.evaluate() == LBool.L_FALSE) {
                            break atoms;
                        }
                    } else {
                        for (Resolver cause : f.getCauses()) {
                            if (cause.in_plan.isConst()) {
                                if (cause.in_plan.evaluate() == LBool.L_FALSE) {
                                    break atoms;
                                }
                            } else {
                                and.add(cause.in_plan);
                                queue.add(cause.effect);
                            }
                        }
                    }
                }
                and.add(solver.eq(atom.state, AtomState.Unified));
                and.add(solver.eq(a.state, AtomState.Active));
                and.add(atom.eq(a));
                BoolExpr eq = solver.and(and.toArray(new BoolExpr[and.size()]));
                try {
                    if (solver.check(eq)) {
                        // unification is actually possible!
                        UnifyGoal unify = new UnifyGoal(solver, this, atom, a, eq);
                        rs.add(unify);
                        boolean add_pre = unify.addPrecondition(solver.reasons.get(a));
                        assert add_pre;
                        solver.setCost(this, solver.getCost(solver.reasons.get(a)));
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
        } else {
            rs.add(new ExpandGoal(solver, this, atom));
        }
        return true;
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

        AddFact(Solver s, Flaw e, Atom atom) {
            super(s, s.newReal(0), e);
            this.atom = atom;
        }

        @Override
        protected boolean apply() {
            return solver.activateFact(atom) && solver.add(solver.imply(in_plan, solver.eq(((AtomFlaw) effect).atom.state, AtomState.Active)));
        }

        @Override
        public String toSimpleString() {
            return "add fact";
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
            return solver.activateGoal(atom) && solver.add(solver.imply(in_plan, solver.eq(((AtomFlaw) effect).atom.state, AtomState.Active))) && ((Predicate) ((AtomFlaw) effect).atom.type).apply(((AtomFlaw) effect).atom);
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

        UnifyGoal(Solver s, Flaw e, Atom unifying, Atom with, BoolExpr eq_expr) {
            super(s, s.newReal(0), e);
            this.unifying = unifying;
            this.with = with;
            this.eq_expr = eq_expr;
        }

        @Override
        protected boolean apply() {
            return (((AtomFlaw) effect).fact ? solver.unifyFact(unifying, with) : solver.unifyGoal(unifying, with)) && solver.add(solver.imply(in_plan, eq_expr));
        }

        @Override
        public String toSimpleString() {
            return "unify";
        }
    }
}
