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
package it.cnr.istc.oratio.solver.gui;

import it.cnr.istc.oratio.solver.Flaw;
import java.awt.Frame;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class FlawJDialog extends javax.swing.JDialog {

    private final Flaw flaw;

    /**
     * Creates new form FlawJDialog
     */
    public FlawJDialog(Flaw flaw) {
        super((Frame) null, false);
        initComponents();
        this.flaw = flaw;
        setTitle(flaw.toSimpleString());
        nameJTextField.setText(flaw.toSimpleString());
        costJTextField.setText(Double.toString(flaw.getSolver().getCost(flaw)));
        switch (flaw.getInPlan().evaluate()) {
            case L_TRUE:
                inPlanJTextField.setText("True");
                break;
            case L_FALSE:
                inPlanJTextField.setText("False");
                break;
            case L_UNKNOWN:
                inPlanJTextField.setText("Unknown");
                break;
            default:
                throw new AssertionError(flaw.getInPlan().evaluate().name());
        }
        detailsJScrollPane.setViewportView(flaw.getDetails());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameJLabel = new javax.swing.JLabel();
        nameJTextField = new javax.swing.JTextField();
        costJLabel = new javax.swing.JLabel();
        costJTextField = new javax.swing.JTextField();
        inPlanJLabel = new javax.swing.JLabel();
        inPlanJTextField = new javax.swing.JTextField();
        detailsJScrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        nameJLabel.setText("Name:");

        nameJTextField.setEditable(false);

        costJLabel.setText("Estimated cost:");

        costJTextField.setEditable(false);

        inPlanJLabel.setText("In plan:");

        inPlanJTextField.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailsJScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameJLabel)
                            .addComponent(inPlanJLabel)
                            .addComponent(costJLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inPlanJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                            .addComponent(costJTextField)
                            .addComponent(nameJTextField))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameJLabel)
                    .addComponent(nameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(costJLabel)
                    .addComponent(costJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inPlanJLabel)
                    .addComponent(inPlanJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailsJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel costJLabel;
    private javax.swing.JTextField costJTextField;
    private javax.swing.JScrollPane detailsJScrollPane;
    private javax.swing.JLabel inPlanJLabel;
    private javax.swing.JTextField inPlanJTextField;
    private javax.swing.JLabel nameJLabel;
    private javax.swing.JTextField nameJTextField;
    // End of variables declaration//GEN-END:variables
}
