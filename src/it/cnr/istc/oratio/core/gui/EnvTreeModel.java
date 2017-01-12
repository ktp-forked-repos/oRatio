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
package it.cnr.istc.oratio.core.gui;

import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.Core;
import it.cnr.istc.oratio.core.Field;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IEnv;
import it.cnr.istc.oratio.core.IItem;
import it.cnr.istc.oratio.core.Predicate;
import it.cnr.istc.oratio.core.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class EnvTreeModel extends DefaultTreeModel {

    private Core core;

    public EnvTreeModel() {
        super(new DefaultMutableTreeNode());
    }

    public void setEnv(IEnv env) {
        this.core = env.getCore();
        if (env instanceof Atom) {
            setRoot(new AtomNode((Atom) env));
        } else if (env instanceof IItem) {
            setRoot(new ItemNode("root", (IItem) env));
        } else {
            setRoot(new DefaultMutableTreeNode());
        }
        createChilds((DefaultMutableTreeNode) root);
        fireTreeNodesChanged(this, null, null, null);
    }

    public void createChilds(DefaultMutableTreeNode tree_node) {
        tree_node.removeAllChildren();
        if (tree_node instanceof ItemNode) {
            for (Field f : getFields(((ItemNode) tree_node).item.getType())) {
                tree_node.add(new ItemNode(f.name, ((ItemNode) tree_node).item.get(f.name)));
            }
            for (Predicate p : ((ItemNode) tree_node).item.getType().getPredicates()) {
                for (IItem i : p.getInstances()) {
                    if (((IEnumItem) ((Atom) i).get(Core.SCOPE)).getEnumVar().evaluate().getAllowedValues().contains(((ItemNode) tree_node).item)) {
                        tree_node.add(new AtomNode((Atom) i));
                    }
                }
            }
        } else if (tree_node instanceof AtomNode) {
            for (Field f : getFields(((AtomNode) tree_node).atom.getType())) {
                tree_node.add(new ItemNode(f.name, ((AtomNode) tree_node).atom.get(f.name)));
            }
        } else {
            for (Field f : core.getFields()) {
                tree_node.add(new ItemNode(f.name, ((ItemNode) tree_node).item.get(f.name)));
            }
            for (Predicate p : core.getPredicates()) {
                for (IItem i : p.getInstances()) {
                    tree_node.add(new AtomNode((Atom) i));
                }
            }
        }
        int[] child_indices = new int[tree_node.getChildCount()];
        Object[] children = new Object[tree_node.getChildCount()];
        for (int i = 0; i < child_indices.length; i++) {
            child_indices[i] = i;
            children[i] = tree_node.getChildAt(i);
        }
        fireTreeStructureChanged(this, tree_node.getPath(), child_indices, children);
    }

    private static Collection<Field> getFields(Type type) {
        Map<String, Field> fields = new HashMap<>();

        LinkedList<Type> queue = new LinkedList<>();
        queue.add(type);
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            queue.addAll(c_type.getSuperclasses());
            for (Field f : c_type.getFields()) {
                if (!f.synthetic && !fields.containsKey(f.name)) {
                    fields.put(f.name, f);
                }
            }
        }

        return fields.values();
    }

    static class ItemNode extends DefaultMutableTreeNode {

        final String name;
        final IItem item;

        ItemNode(String name, IItem item) {
            super(item, getFields(item.getType()).stream().anyMatch(f -> !f.synthetic));
            this.name = name;
            this.item = item;
        }

        @Override
        public boolean isLeaf() {
            return !getAllowsChildren();
        }

        @Override
        public Object clone() {
            return super.clone();
        }
    }

    static class AtomNode extends DefaultMutableTreeNode {

        final Atom atom;

        AtomNode(Atom atom) {
            super(atom, getFields(atom.getType()).stream().anyMatch(f -> !f.synthetic));
            this.atom = atom;
        }

        @Override
        public boolean isLeaf() {
            return !getAllowsChildren();
        }

        @Override
        public Object clone() {
            return super.clone();
        }
    }
}
