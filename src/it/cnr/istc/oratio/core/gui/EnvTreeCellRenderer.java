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

import it.cnr.istc.ac.EnumDomain;
import it.cnr.istc.oratio.core.Atom;
import it.cnr.istc.oratio.core.AtomState;
import it.cnr.istc.oratio.core.Core;
import it.cnr.istc.oratio.core.IArithItem;
import it.cnr.istc.oratio.core.IBoolItem;
import it.cnr.istc.oratio.core.IEnumItem;
import it.cnr.istc.oratio.core.IStringItem;
import it.cnr.istc.oratio.core.Type;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class EnvTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final ImageIcon OBJECT_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/object.png"));
    private static final ImageIcon ACTIVE_ATOM_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/formula.png"));
    private static final ImageIcon INACTIVE_ATOM_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/inactive_formula.png"));
    private static final ImageIcon UNIFIED_ATOM_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/unified_formula.png"));
    private static final ImageIcon ENUM_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/enum.png"));
    private static final ImageIcon BOOL_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/bool.png"));
    private static final ImageIcon NUMBER_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/number.png"));
    private static final ImageIcon STRING_ICON = new ImageIcon(EnvTreeCellRenderer.class.getResource("/it/cnr/istc/oratio/core/gui/resources/enum.png"));

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof EnvTreeModel.ItemNode) {
            Type type = ((EnvTreeModel.ItemNode) value).item.getType();
            switch (type.name) {
                case Core.BOOL:
                    setIcon(BOOL_ICON);
                    setText(((EnvTreeModel.ItemNode) value).name + " = " + ((IBoolItem) ((EnvTreeModel.ItemNode) value).item).getBoolVar().evaluate());
                    break;
                case Core.REAL:
                    setIcon(NUMBER_ICON);
                    setText(((EnvTreeModel.ItemNode) value).name + " = " + ((EnvTreeModel.ItemNode) value).item.getCore().evaluate(((IArithItem) ((EnvTreeModel.ItemNode) value).item).getArithVar()));
                    break;
                case Core.STRING:
                    setIcon(STRING_ICON);
                    setText(((EnvTreeModel.ItemNode) value).name + " = " + ((IStringItem) ((EnvTreeModel.ItemNode) value).item).getValue());
                    break;
                default:
                    if (value instanceof IEnumItem) {
                        setIcon(ENUM_ICON);
                    } else {
                        setIcon(OBJECT_ICON);
                    }
                    setText(((EnvTreeModel.ItemNode) value).name);
                    break;
            }
        } else if (value instanceof EnvTreeModel.AtomNode) {
            Atom atom = ((EnvTreeModel.AtomNode) value).atom;
            EnumDomain<AtomState> state = atom.state.evaluate();
            if (state.isSingleton()) {
                switch (state.getAllowedValues().iterator().next()) {
                    case Active:
                        setIcon(ACTIVE_ATOM_ICON);
                        break;
                    case Inactive:
                        setIcon(INACTIVE_ATOM_ICON);
                        break;
                    case Unified:
                        setIcon(UNIFIED_ATOM_ICON);
                        break;
                    default:
                        throw new AssertionError(state.getAllowedValues().iterator().next().name());
                }
            } else {
                setIcon(INACTIVE_ATOM_ICON);
            }
            setText(atom.type.name);
        }
        return this;
    }
}
