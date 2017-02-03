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
import it.cnr.istc.ac.LBool;
import it.cnr.istc.core.Atom;
import it.cnr.istc.core.Core;
import it.cnr.istc.core.Disjunction;
import it.cnr.istc.core.IEnumItem;
import it.cnr.istc.core.IEnv;
import it.cnr.istc.core.IItem;
import it.cnr.istc.core.Type;
import it.cnr.istc.iloc.types.StateVariable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver extends Core {

    private static final Logger LOG = Logger.getLogger(Solver.class.getName());
    private final LinkedList<Node> fringe = new LinkedList<>();
    private Node current_node;
    private final Collection<SolverListener> listeners = new ArrayList<>();

    public Solver() {
        try {
            boolean read = read(new File(Solver.class.getResource("time.rddl").getPath()));
            assert read;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        types.put(StateVariable.NAME, new StateVariable(this));

        current_node = new Node(this);
        boolean add = add(current_node.resolver.in_plan);
        assert add;
        ctr_var = current_node.resolver.in_plan;
        fringe.add(current_node);
    }

    @Override
    public IEnumItem newEnumItem(Type type, IItem... values) {
        IEnumItem c_enum = super.newEnumItem(type, values);
        EnumFlaw flaw = new EnumFlaw(this, c_enum);
        current_node.flaws.add(flaw);
        listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
        return c_enum;
    }

    @Override
    protected boolean newFact(Atom atom) {
        if (!super.newFact(atom)) {
            return false;
        }
        AtomFlaw flaw = new AtomFlaw(this, atom, true);
        current_node.flaws.add(flaw);
        listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
        return true;
    }

    @Override
    protected boolean activateFact(Atom atom) {
        return super.activateFact(atom);
    }

    @Override
    protected boolean unifyFact(Atom unifying, Atom with) {
        return super.unifyFact(unifying, with);
    }

    @Override
    protected boolean newGoal(Atom atom) {
        if (!super.newGoal(atom)) {
            return false;
        }
        AtomFlaw flaw = new AtomFlaw(this, atom, false);
        current_node.flaws.add(flaw);
        listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
        return true;
    }

    @Override
    protected boolean activateGoal(Atom atom) {
        return super.activateGoal(atom);
    }

    @Override
    protected boolean unifyGoal(Atom unifying, Atom with) {
        return super.unifyGoal(unifying, with);
    }

    @Override
    public boolean newDisjunction(IEnv env, Disjunction d) {
        if (!super.newDisjunction(env, d)) {
            return false;
        }
        DisjunctionFlaw flaw = new DisjunctionFlaw(this, env, d);
        current_node.flaws.add(flaw);
        listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
        return true;
    }

    @Override
    public boolean solve() {
        try {
            while (!fringe.isEmpty()) {
                Node node = fringe.poll();
                if (node.resolver.in_plan.evaluate() != LBool.L_FALSE) {
                    if (go_to(node)) {
                        assert current_node == node;
                        Flaw flaw = null;
                        Collection<Flaw> inconsistencies = get_inconsistencies();
                        if (!inconsistencies.isEmpty()) {
                            flaw = inconsistencies.stream().findAny().get();
                        } else if (!node.flaws.isEmpty()) {
                            flaw = node.flaws.stream().findAny().get();
                            node.flaws.remove(flaw);
                            listeners.parallelStream().forEach(listener -> listener.currentNode(node));
                        }
                        if (flaw != null) {
                            if (flaw.expand()) {
                                Collection<Resolver> resolvers = flaw.getResolvers();
                                if (resolvers.size() == 1) {
                                    // we have a trivial node..
                                    Resolver resolver = resolvers.iterator().next();
                                    ctr_var = resolver.in_plan;
                                    if (!add(resolver.in_plan) || !resolver.apply()) {
                                        // the problem is unsolvable..
                                        return false;
                                    }
                                    // we re-add the node at the beginining to be repolled at next ycle..
                                    fringe.addFirst(node);
                                } else {
                                    if (!add(flaw.isDisjunctive() ? exct_one(resolvers.stream().map(resolver -> resolver.in_plan).toArray(BoolExpr[]::new)) : or(resolvers.stream().map(resolver -> resolver.in_plan).toArray(BoolExpr[]::new)))) {
                                        // the problem is unsolvable..
                                        return false;
                                    }
                                    List<Node> childs = resolvers.stream().map(resolver -> new Node(node, resolver)).collect(Collectors.toList());
                                    Collections.reverse(childs);
                                    for (Node n : childs) {
                                        fringe.addFirst(n);
                                    }
                                    listeners.parallelStream().forEach(listener -> listener.branch(node, childs));
                                    for (Node n : childs) {
                                        current_node = n;
                                        ctr_var = n.resolver.in_plan;
                                        if (!add(imply(n.resolver.in_plan, node.resolver.in_plan)) || !n.resolver.apply()) {
                                            // the problem is unsolvable..
                                            return false;
                                        }
                                    }
                                    current_node = node;
                                    listeners.parallelStream().forEach(listener -> listener.currentNode(node));
                                }
                            }
                        } else {
                            // we have found a solution..
                            listeners.parallelStream().forEach(listener -> listener.solutionNode(current_node));
                            return true;
                        }
                    }
                }
            }
        } catch (InconsistencyException e) {
            // the problem is unsolvable..
            return false;
        }
        // the problem is unsolvable..
        return false;
    }

    private boolean go_to(Node node) throws InconsistencyException {
        if (current_node == node) {
            return true;
        } else if (node.parent == current_node) {
            push(node.resolver);
            current_node = node;
            listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
            return assign(current_node.resolver.in_plan);
        } else {
            // we look for a common ancestor c_node..
            Node c_node = current_node;
            Set<Node> parents = new HashSet<>(c_node.level);
            while (c_node != null) {
                parents.add(c_node);
                c_node = c_node.parent;
            }
            c_node = node;
            List<Node> path = new ArrayList<>();
            while (c_node != null && !parents.contains(c_node)) {
                path.add(c_node);
                c_node = c_node.parent;
            }
            Collections.reverse(path);

            // we pop till the common ancestor..
            while (current_node.level > c_node.level) {
                pop();
                listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
            }

            // we push till the target node..
            for (Node n : path) {
                push(n.resolver);
                current_node = n;
                listeners.parallelStream().forEach(listener -> listener.currentNode(current_node));
                if (!assign(current_node.resolver.in_plan)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Collects all the inconsistencies of the {@link SmartType} instances.
     *
     * @return a collection of {@link Flaw}s representing all the
     * inconsistencies.
     */
    private Collection<Flaw> get_inconsistencies() {
        Set<Type> c_types = new HashSet<>();
        LinkedList<Type> queue = new LinkedList<>();
        queue.addAll(types.values());
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            if (!c_types.contains(c_type)) {
                c_types.add(c_type);
                queue.addAll(c_type.getTypes());
            }
        }
        Collection<Flaw> c_flaws = new ArrayList<>();
        for (Type type : c_types) {
            if (type instanceof SmartType) {
                c_flaws.addAll(((SmartType) type).getInconsistencies());
            }
        }
        return c_flaws;
    }

    private void push(Resolver r) {
        super.push();
        current_node = new Node(current_node, r);
    }

    @Override
    public void push() {
        super.push();
        current_node = new Node(current_node, null);
    }

    @Override
    public void pop() {
        super.pop();
        current_node = current_node.parent;
    }

    public Node getCurrentNode() {
        return current_node;
    }

    public void addSolverListener(SolverListener listener) {
        listeners.add(listener);
        listener.init(this);
    }

    public void removeSolverListener(SolverListener listener) {
        listeners.remove(listener);
    }

    public static class Node {

        private Solver solver;
        private final int level;
        private final Node parent;
        private final Resolver resolver;
        private final Collection<Flaw> flaws;

        Node(Solver solver) {
            this.solver = solver;
            this.level = 0;
            this.parent = null;
            this.resolver = new Resolver(solver, solver.newReal(0), null) {
                @Override
                protected boolean apply() {
                    return solver.add(in_plan);
                }

                @Override
                public String toSimpleString() {
                    return "root";
                }
            };
            this.flaws = new ArrayList<>();
        }

        Node(Node parent, Resolver resolver) {
            this.solver = parent.solver;
            this.level = parent.level + 1;
            this.parent = parent;
            this.resolver = resolver;
            this.flaws = new ArrayList<>(parent.flaws);
        }

        public int getLevel() {
            return level;
        }

        public Node getParent() {
            return parent;
        }

        public Resolver getResolver() {
            return resolver;
        }

        public Collection<Flaw> getFlaws() {
            return Collections.unmodifiableCollection(flaws);
        }
    }
}
