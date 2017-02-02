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
package it.cnr.istc.iloc.gui;

import it.cnr.istc.core.Atom;
import it.cnr.istc.core.AtomState;
import static it.cnr.istc.core.Core.BOOL;
import static it.cnr.istc.core.Core.REAL;
import static it.cnr.istc.core.Core.STRING;
import it.cnr.istc.core.Field;
import it.cnr.istc.core.IArithItem;
import it.cnr.istc.core.IBoolItem;
import it.cnr.istc.core.IEnumItem;
import it.cnr.istc.core.IItem;
import static it.cnr.istc.core.IScope.SCOPE;
import it.cnr.istc.core.IStringItem;
import it.cnr.istc.core.Type;
import it.cnr.istc.iloc.SmartType;
import it.cnr.istc.iloc.types.StateVariable;
import it.cnr.istc.utils.gui.ReverseGradientXYBarPainter;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalDataItem;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
class StateVariableVisualizer implements TimelineVisualizer {

    @Override
    public Class<? extends SmartType> getType() {
        return StateVariable.class;
    }

    @Override
    public Collection<XYPlot> getPlots(Type type) {
        Collection<IItem> instances = type.getInstances();
        Collection<XYPlot> plots = new ArrayList<>(instances.size());
        Map<IItem, Collection<Atom>> sv_atoms = new IdentityHashMap<>(instances.size());
        for (IItem i : type.getInstances()) {
            sv_atoms.put(i, new ArrayList<>());
        }
        for (Atom atom : ((StateVariable) type).getDefinedPredicates().stream().flatMap(p -> p.getInstances().stream()).map(a -> (Atom) a).filter(a -> a.state.evaluate().isSingleton() && a.state.evaluate().contains(AtomState.Active)).collect(Collectors.toList())) {
            for (IItem i : ((IEnumItem) atom.get(SCOPE)).getEnumVar().evaluate().getAllowedValues()) {
                if (sv_atoms.containsKey(i)) {
                    sv_atoms.get(i).add(atom);
                }
            }
        }

        for (IItem sv : instances) {
            Collection<Atom> atoms = sv_atoms.get(sv);
            // For each pulse the atoms starting at that pulse
            Map<Double, Collection<Atom>> starting_atoms = new HashMap<>(atoms.size());
            // For each pulse the atoms ending at that pulse
            Map<Double, Collection<Atom>> ending_atoms = new HashMap<>(atoms.size());

            // The pulses of the timeline
            Set<Double> c_pulses = new HashSet<>(atoms.size() * 2);

            for (Atom atom : atoms) {
                double start = sv.getCore().evaluate(((IArithItem) atom.get("start")).getArithVar());
                double end = sv.getCore().evaluate(((IArithItem) atom.get("end")).getArithVar());

                if (!starting_atoms.containsKey(start)) {
                    starting_atoms.put(start, new ArrayList<>());
                }
                starting_atoms.get(start).add(atom);

                if (!ending_atoms.containsKey(end)) {
                    ending_atoms.put(end, new ArrayList<>());
                }
                ending_atoms.get(end).add(atom);

                c_pulses.add(start);
                c_pulses.add(end);
            }

            // we sort current pulses..
            Double[] c_pulses_array = c_pulses.toArray(new Double[c_pulses.size()]);
            Arrays.sort(c_pulses_array);

            XYIntervalSeriesCollection collection = new XYIntervalSeriesCollection();

            ValueXYIntervalSeries undefined = new ValueXYIntervalSeries("Undefined");
            ValueXYIntervalSeries sv_values = new ValueXYIntervalSeries("Values");
            ValueXYIntervalSeries conflicts = new ValueXYIntervalSeries("Conflicts");

            List<Atom> overlapping_atoms = new ArrayList<>();
            double start = 0;
            for (Double p : c_pulses_array) {
                if (starting_atoms.containsKey(p)) {
                    overlapping_atoms.addAll(starting_atoms.get(p));
                }
                if (ending_atoms.containsKey(p)) {
                    overlapping_atoms.removeAll(ending_atoms.get(p));
                }
                switch (overlapping_atoms.size()) {
                    case 0:
                        undefined.add(start, start, p, 0, 0, 1, new Atom[0]);
                        break;
                    case 1:
                        sv_values.add(start, start, p, 0, 0, 1, overlapping_atoms.toArray(new Atom[overlapping_atoms.size()]));
                        break;
                    default:
                        sv_values.add(start, start, p, 0, 0, 1, overlapping_atoms.toArray(new Atom[overlapping_atoms.size()]));
                        break;
                }
            }

            collection.addSeries(undefined);
            collection.addSeries(sv_values);
            collection.addSeries(conflicts);

            XYBarRenderer renderer = new XYBarRenderer();
            renderer.setSeriesPaint(0, Color.lightGray);
            renderer.setSeriesPaint(1, new Color(100, 250, 100));
            renderer.setSeriesPaint(2, Color.pink);
            renderer.setBarPainter(new ReverseGradientXYBarPainter());
            renderer.setDrawBarOutline(true);
            renderer.setShadowXOffset(2);
            renderer.setShadowYOffset(2);
            renderer.setUseYInterval(true);

            renderer.setBaseItemLabelsVisible(true);
            renderer.setBaseItemLabelPaint(Color.black);
            Font font = new Font("SansSerif", Font.PLAIN, 9);
            renderer.setBaseItemLabelFont(font);
            XYItemLabelGenerator generator = (XYDataset dataset, int series, int item) -> toString(((ValueXYIntervalDataItem) ((XYIntervalSeriesCollection) dataset).getSeries(series).getDataItem(item)).atoms);
            ItemLabelPosition itLabPos = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
            renderer.setBasePositiveItemLabelPosition(itLabPos);
            for (int i = 0; i < collection.getSeriesCount(); i++) {
                renderer.setSeriesItemLabelGenerator(i, generator);
                renderer.setSeriesItemLabelsVisible(i, true);
                renderer.setSeriesItemLabelPaint(i, Color.black);
                renderer.setSeriesItemLabelFont(i, font);
                renderer.setSeriesPositiveItemLabelPosition(i, itLabPos);
                renderer.setSeriesToolTipGenerator(i, (XYDataset dataset, int series, int item) -> toString(((ValueXYIntervalDataItem) ((XYIntervalSeriesCollection) dataset).getSeries(series).getDataItem(item)).atoms));
            }

            XYPlot plot = new XYPlot(collection, null, new NumberAxis(""), renderer);
            plot.getRangeAxis().setVisible(false);
            plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

            plots.add(plot);
        }

        return plots;
    }

    @Override
    public void mouseClicked(Object dataItem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static String toString(Atom atom) {
        StringBuilder sb = new StringBuilder();
        sb.append(atom.type.name).append("(");
        for (Field field : atom.type.getFields()) {
            if (!field.synthetic && !field.name.equals(SCOPE)) {
                IItem item = atom.get(field.name);
                sb.append(", ").append(field.name);
                switch (field.type.name) {
                    case BOOL:
                        sb.append(" = ").append(((IBoolItem) item).getBoolVar().evaluate());
                        break;
                    case REAL:
                        sb.append(" = ").append(atom.core.evaluate(((IArithItem) item).getArithVar()));
                        break;
                    case STRING:
                        sb.append(" = ").append(((IStringItem) item).getValue());
                        break;
                }
            }
        }
        sb.append(")");
        return sb.toString().replace("(, ", "(");
    }

    private static String toString(Atom[] atoms) {
        switch (atoms.length) {
            case 0:
                return "";
            case 1:
                return toString(atoms[0]);
            default:
                return Stream.of(atoms).map(tk -> toString(tk)).collect(Collectors.joining(", "));
        }
    }

    private static class ValueXYIntervalSeries extends XYIntervalSeries {

        private ValueXYIntervalSeries(Comparable<?> key) {
            super(key);
        }

        public void add(double x, double xLow, double xHigh, double y, double yLow, double yHigh, Atom[] atoms) {
            super.add(new ValueXYIntervalDataItem(x, xLow, xHigh, y, yLow, yHigh, atoms), true);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class ValueXYIntervalDataItem extends XYIntervalDataItem {

        private final Atom[] atoms;

        private ValueXYIntervalDataItem(double x, double xLow, double xHigh, double y, double yLow, double yHigh, Atom[] atoms) {
            super(x, xLow, xHigh, y, yLow, yHigh);
            this.atoms = atoms;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
