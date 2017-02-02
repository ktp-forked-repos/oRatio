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

import it.cnr.istc.iloc.Solver;
import it.cnr.istc.iloc.SolverListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tree;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.DecoratorItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 *
 * @author Riccardo De Benedictis <riccardo.debenedictis@istc.cnr.it>
 */
public class ResolutionTreeDisplay extends Display implements SolverListener {

    private static final String GRAPH = "tree";
    private static final String NODES = "tree.nodes";
    private static final String EDGES = "tree.edges";
    private static final String NODE_TYPE = "node_type";
    private static final String NODE_COST = "node_cost";
    private static final String NODE_CONTENT = "node_content";
    private static final String EDGE_DECORATORS = "edgeDeco";
    private static final String NODE_DECORATORS = "nodeDeco";
    private static final Schema DECORATOR_SCHEMA = PrefuseLib.getVisualItemSchema();
    private static final Executor EXECUTOR = Executors.newCachedThreadPool();

    static {
        DECORATOR_SCHEMA.setDefault(VisualItem.INTERACTIVE, false);
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        DECORATOR_SCHEMA.setDefault(VisualItem.FONT, FontLib.getFont("Tahoma", 7));
    }
    private final Tree t = new Tree();
    private final VisualGraph vg;
    private final Map<Solver.Node, Node> nodes = new IdentityHashMap<>();

    public ResolutionTreeDisplay() {
        // initialize display and data
        super(new Visualization());

        Logger.getLogger("prefuse").setLevel(Level.OFF);

        t.getNodeTable().addColumn(VisualItem.LABEL, String.class);
        t.getNodeTable().addColumn(NODE_TYPE, String.class);
        t.getNodeTable().addColumn(NODE_COST, Integer.class);
        t.getNodeTable().addColumn(NODE_CONTENT, Object.class);
        t.getEdgeTable().addColumn(VisualItem.LABEL, String.class);

        // add visual data groups
        vg = m_vis.addGraph(GRAPH, t);

        m_vis.setInteractive(EDGES, null, false);
        m_vis.setValue(NODES, null, VisualItem.SHAPE, Constants.SHAPE_ELLIPSE);

        // set up the renderers
        // draw the nodes as basic shapes
        LabelRenderer nodeR = new LabelRenderer(VisualItem.LABEL);
        nodeR.setRoundedCorner(8, 8);

        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        drf.setDefaultEdgeRenderer(new EdgeRenderer(Constants.EDGE_TYPE_CURVE));
        m_vis.setRendererFactory(drf);

        // adding decorators, one group for the nodes, one for the edges
        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(50));
        m_vis.addDecorators(EDGE_DECORATORS, EDGES, DECORATOR_SCHEMA);

        DECORATOR_SCHEMA.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(128));
        m_vis.addDecorators(NODE_DECORATORS, NODES, DECORATOR_SCHEMA);

        // set up the visual operators
        // first set up all the color actions
        ColorAction nFill = new DataColorAction(NODES, NODE_COST, Constants.ORDINAL, VisualItem.FILLCOLOR, ColorLib.getHotPalette());
        nFill.add(VisualItem.HOVER, ColorLib.gray(200));
        nFill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 230, 230));
        nFill.add(NODE_TYPE + " == \"default\"", ColorLib.rgb(230, 230, 250));
        nFill.add(NODE_TYPE + " == \"current\"", ColorLib.rgb(255, 255, 204));
        nFill.add(NODE_TYPE + " == \"inconsistent\"", ColorLib.rgb(255, 69, 0));
        nFill.add(NODE_TYPE + " == \"solution\"", ColorLib.rgb(152, 251, 152));

        ColorAction nStroke = new ColorAction(NODES, VisualItem.STROKECOLOR);
        nStroke.setDefaultColor(ColorLib.gray(100));
        nStroke.add(VisualItem.HOVER, ColorLib.gray(50));

        ColorAction eStroke = new ColorAction(EDGES, VisualItem.STROKECOLOR);
        eStroke.setDefaultColor(ColorLib.gray(100));

        ColorAction eFill = new ColorAction(EDGES, VisualItem.FILLCOLOR);
        eFill.setDefaultColor(ColorLib.gray(100));

        // bundle the color actions
        ActionList colors = new ActionList();
        colors.add(nStroke);
        colors.add(nFill);
        colors.add(eStroke);
        colors.add(eFill);

        NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(GRAPH, Constants.ORIENT_TOP_BOTTOM, 50, 250, 250);
        treeLayout.setLayoutAnchor(new Point2D.Double());

        // now create the main layout routine
        ActionList layout = new ActionList(Activity.INFINITY);
        layout.add(colors);
        layout.add(new LabelLayout2(EDGE_DECORATORS));
        layout.add(new LabelLayout2(NODE_DECORATORS));
        layout.add(treeLayout);
        layout.add(new RepaintAction());
        m_vis.putAction("layout", layout);

        // set up the display
        setHighQuality(true);
        addControlListener(new PanControl());
        addControlListener(new ZoomToFitControl());
        addControlListener(new WheelZoomControl());
        addControlListener(new ControlAdapter() {
            @Override
            public void itemEntered(VisualItem vi, MouseEvent me) {
                Display d = (Display) me.getSource();
                if (vi.getSourceTuple() instanceof Node) {
                    Node nodeData = (Node) vi.getSourceTuple();
                    Object content = nodeData.get(NODE_CONTENT);
                    if (content instanceof Solver.Node) {
                        Solver.Node c_node = (Solver.Node) content;
                        d.setToolTipText(c_node.toString());
                    }
                }
            }

            @Override
            public void itemExited(VisualItem vi, MouseEvent me) {
                Display d = (Display) me.getSource();
                d.setToolTipText(null);
            }
        });
    }

    @Override
    public void init(Solver solver) {
        synchronized (m_vis) {
            t.getEdges().clear();
            t.getNodes().clear();
            nodes.clear();
            Node root = t.addRoot();
            Solver.Node node = solver.getCurrentNode();
            root.set(VisualItem.LABEL, Integer.toString(node.getFlaws().size()));
            root.set(NODE_TYPE, "default");
            root.set(NODE_CONTENT, solver.getCurrentNode());
            Rectangle2D bounds = m_vis.getVisualItem(NODES, root).getBounds();
            panToAbs(new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()));
            nodes.put(node, root);
        }

        // set things running
        m_vis.run("layout");
    }

    @Override
    public void currentNode(Solver.Node n) {
        synchronized (m_vis) {
            nodes.get(n).set(VisualItem.LABEL, Integer.toString(n.getFlaws().size()));
            if (!m_vis.getVisualItem(NODES, nodes.get(n)).isHighlighted()) {
                EXECUTOR.execute(() -> {
                    synchronized (m_vis) {
                        m_vis.getVisualItem(NODES, nodes.get(n)).setHighlighted(true);
                    }
                    try {
                        Thread.sleep(2_000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ResolutionTreeDisplay.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    synchronized (m_vis) {
                        m_vis.getVisualItem(NODES, nodes.get(n)).setHighlighted(false);
                    }
                });
            }
        }
    }

    @Override
    public void inconsistentNode(Solver.Node n) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "inconsistent");
        }
    }

    @Override
    public void solutionNode(Solver.Node n) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "solution");
        }
    }

    @Override
    public void branch(Solver.Node n, List<Solver.Node> childs) {
        synchronized (m_vis) {
            nodes.get(n).set(NODE_TYPE, "default");
            for (Solver.Node child : childs) {
                Node c_c = t.addChild(nodes.get(n));
                c_c.set(VisualItem.LABEL, Integer.toString(n.getFlaws().size()));
                c_c.set(NODE_TYPE, "default");
                c_c.set(NODE_CONTENT, child);
                c_c.getParentEdge().set(VisualItem.LABEL, child.getResolver().toSimpleString());
                nodes.put(child, c_c);
            }
        }
    }

    /**
     * Set label positions. Labels are assumed to be DecoratorItem instances,
     * decorating their respective nodes. The layout simply gets the bounds of
     * the decorated node and assigns the label coordinates to the center of
     * those bounds.
     */
    private static class LabelLayout2 extends Layout {

        LabelLayout2(String group) {
            super(group);
        }

        @Override
        public void run(double frac) {
            Iterator<?> iter = m_vis.items(m_group);
            while (iter.hasNext()) {
                DecoratorItem decorator = (DecoratorItem) iter.next();
                VisualItem decoratedItem = decorator.getDecoratedItem();
                Rectangle2D bounds = decoratedItem.getBounds();

                double x = bounds.getCenterX();
                double y = bounds.getCenterY();

                setX(decorator, null, x);
                setY(decorator, null, y);
            }
        }
    }
}
