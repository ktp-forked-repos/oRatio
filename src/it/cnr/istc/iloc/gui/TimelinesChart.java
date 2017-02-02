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

import it.cnr.istc.core.Type;
import it.cnr.istc.iloc.Solver;
import it.cnr.istc.iloc.SolverListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public final class TimelinesChart extends ChartPanel implements SolverListener {

    private static final JFreeChart CHART = new JFreeChart(new XYPlot());
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private final Map<Class<? extends Type>, TimelineVisualizer> visualizers = new IdentityHashMap<>();
    private Solver solver;

    public TimelinesChart() {
        super(CHART);
        setBorder(null);

        addTimelineVisualizer(new StateVariableVisualizer());

        addChartMouseListener(new ChartMouseListener() {
            private Object getDataItem(ChartEntity entity) {
                XYItemEntity e = ((XYItemEntity) entity);
                XYDataset ds = e.getDataset();
                if (ds instanceof XYIntervalSeriesCollection) {
                    XYIntervalSeries theSerie = ((XYIntervalSeriesCollection) ds).getSeries(e.getSeriesIndex());
                    return theSerie.getDataItem(e.getItem());
                } else if (ds instanceof XYSeriesCollection) {
                    XYSeries theSerie = ((XYSeriesCollection) ds).getSeries(e.getSeriesIndex());
                    return theSerie.getDataItem(e.getItem());
                } else {
                    return null;
                }
            }

            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                if (cme.getEntity() != null && cme.getEntity() instanceof XYItemEntity) {
                    Object dataItem = getDataItem(cme.getEntity());
                    visualizers.values().forEach(visualizer -> {
                        visualizer.mouseClicked(dataItem);
                    });
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                if (cme.getEntity() != null && cme.getEntity() instanceof XYItemEntity) {
                    setCursor(HAND_CURSOR);
                } else {
                    setCursor(DEFAULT_CURSOR);
                }
            }
        });
    }

    public void addTimelineVisualizer(TimelineVisualizer timelineVisualizer) {
        visualizers.put(timelineVisualizer.getType(), timelineVisualizer);
    }

    @Override
    public void init(Solver solver) {
        this.solver = solver;
    }

    @Override
    public void currentNode(Solver.Node n) {
        final CombinedDomainXYPlot combined_plot = new CombinedDomainXYPlot(new DateAxis("Time"));
        combined_plot.setGap(3.0);
        combined_plot.setOrientation(PlotOrientation.VERTICAL);

        Set<Type> c_types = new HashSet<>();
        LinkedList<Type> queue = new LinkedList<>();
        queue.addAll(solver.getTypes());
        while (!queue.isEmpty()) {
            Type c_type = queue.pollFirst();
            if (!c_types.contains(c_type)) {
                c_types.add(c_type);
                queue.addAll(c_type.getTypes());
            }
        }
        for (Type type : c_types) {
            if (visualizers.containsKey(type.getClass())) {
                for (XYPlot plot : visualizers.get(type.getClass()).getPlots(type)) {
                    TextTitle title = new TextTitle(type.name, new Font("SansSerif", Font.PLAIN, 11), Color.BLACK, RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, new RectangleInsets(4, 4, 4, 4));
                    XYTitleAnnotation titleAnn = new XYTitleAnnotation(0.01, 1, title, RectangleAnchor.TOP_LEFT);
                    plot.addAnnotation(titleAnn);
                    combined_plot.add(plot, 1);
                }
            }
        }
        setBorder(BorderFactory.createEtchedBorder());
    }

    @Override
    public void inconsistentNode(Solver.Node n) {
        setBorder(BorderFactory.createEtchedBorder(Color.red, null));
    }

    @Override
    public void solutionNode(Solver.Node n) {
    }

    @Override
    public void branch(Solver.Node n, List<Solver.Node> childs) {
    }
}
