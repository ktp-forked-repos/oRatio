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
package it.cnr.istc.core.gui;

import it.cnr.istc.core.IEnv;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class EnvJPanel extends javax.swing.JPanel {

    /**
     * Creates new form ScopeJPanel
     */
    public EnvJPanel() {
        initComponents();
        this.envJTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                envTreeModel.createChilds((DefaultMutableTreeNode) event.getPath().getLastPathComponent());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            }
        });
    }

    public void setEnv(IEnv env) {
        this.envTreeModel.setEnv(env);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itemTreeCellRenderer = new it.cnr.istc.core.gui.EnvTreeCellRenderer();
        envTreeModel = new it.cnr.istc.core.gui.EnvTreeModel();
        envJScrollPane = new javax.swing.JScrollPane();
        envJTree = new javax.swing.JTree();

        itemTreeCellRenderer.setText("itemTreeCellRenderer1");

        envJTree.setModel(envTreeModel);
        envJTree.setCellRenderer(itemTreeCellRenderer);
        envJTree.setRootVisible(false);
        envJScrollPane.setViewportView(envJTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(envJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(envJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane envJScrollPane;
    private javax.swing.JTree envJTree;
    private it.cnr.istc.core.gui.EnvTreeModel envTreeModel;
    private it.cnr.istc.core.gui.EnvTreeCellRenderer itemTreeCellRenderer;
    // End of variables declaration//GEN-END:variables
}
