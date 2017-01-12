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

import it.cnr.istc.oratio.core.IEnv;
import it.cnr.istc.oratio.core.IScope;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ScopeJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ScopeJPanel
     */
    public ScopeJPanel(IScope scope, IEnv env) {
        initComponents();
        this.scopeJTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                scopeTreeModel.createChilds((DefaultMutableTreeNode) event.getPath().getLastPathComponent());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            }
        });
        this.scopeTreeModel.setScope(scope, env);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itemTreeCellRenderer = new it.cnr.istc.oratio.core.gui.ItemTreeCellRenderer();
        scopeTreeModel = new it.cnr.istc.oratio.core.gui.ScopeTreeModel();
        scopeJScrollPane = new javax.swing.JScrollPane();
        scopeJTree = new javax.swing.JTree();

        itemTreeCellRenderer.setText("itemTreeCellRenderer1");

        scopeJTree.setModel(scopeTreeModel);
        scopeJTree.setCellRenderer(itemTreeCellRenderer);
        scopeJTree.setRootVisible(false);
        scopeJScrollPane.setViewportView(scopeJTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scopeJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scopeJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private it.cnr.istc.oratio.core.gui.ItemTreeCellRenderer itemTreeCellRenderer;
    private javax.swing.JScrollPane scopeJScrollPane;
    private javax.swing.JTree scopeJTree;
    private it.cnr.istc.oratio.core.gui.ScopeTreeModel scopeTreeModel;
    // End of variables declaration//GEN-END:variables
}