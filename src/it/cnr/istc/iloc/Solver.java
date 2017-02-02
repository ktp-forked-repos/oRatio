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

import it.cnr.istc.core.Core;
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

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class Solver extends Core {

    private static final Logger LOG = Logger.getLogger(Solver.class.getName());
    private final LinkedList<Node> fringe = new LinkedList<>();
    private Node current_node;

    public Solver() {
        try {
            boolean read = read(new File(Solver.class.getResource("time.rddl").getPath()));
            assert read;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

        types.put(StateVariable.NAME, new StateVariable(this));

        current_node = new Node(this);
        fringe.add(current_node);
    }

    @Override
    public boolean solve() {
        while (!fringe.isEmpty()) {
            Node node = fringe.poll();
            if (!go_to(node)) {

            }
        }
        // the problem is unsolvable..
        return false;
    }

    private boolean go_to(Node node) {
        if (current_node == node) {
            return true;
        } else if (node.parent == current_node) {
            push();
            current_node = node;
            return assign(node.resolver.in_plan);
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
            }

            // we push till the target node..
            for (Node n : path) {
                push();
                current_node = n;
                if (!assign(n.resolver.in_plan)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void pop() {
        super.pop();
        current_node = current_node.parent;
    }

    private static class Node {

        private Solver solver;
        private final int level;
        private final Node parent;
        private final Resolver resolver;
        private final Collection<Flaw> flaws;

        Node(Solver solver) {
            this.solver = solver;
            this.level = 0;
            this.parent = null;
            this.resolver = null;
            this.flaws = new ArrayList<>();
        }

        Node(Node parent, Resolver resolver) {
            this.solver = parent.solver;
            this.level = parent.level + 1;
            this.parent = parent;
            this.resolver = resolver;
            this.flaws = new ArrayList<>(parent.flaws);
        }
    }
}
