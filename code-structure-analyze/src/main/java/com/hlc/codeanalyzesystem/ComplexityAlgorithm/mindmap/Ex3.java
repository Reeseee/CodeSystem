package com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;

public class Ex3 {
	Factory<Integer> edgeFactory = new Factory<Integer>() {
		int i = 0;
		public Integer create() {
			return i++;
		}
	};

	public Ex3() {
		// create tree
		DelegateTree<String, Integer> g = new DelegateTree<String, Integer>();
		g.addVertex("V0");
		g.addEdge(edgeFactory.create(), "V0", "V1");
		g.addEdge(edgeFactory.create(), "V0", "V2");
		g.addEdge(edgeFactory.create(), "V1", "V4");
		g.addEdge(edgeFactory.create(), "V2", "V3");
		g.addEdge(edgeFactory.create(), "V2", "V5");
		g.addEdge(edgeFactory.create(), "V4", "V6");
		g.addEdge(edgeFactory.create(), "V4", "V7");
		g.addEdge(edgeFactory.create(), "V3", "V8");
		g.addEdge(edgeFactory.create(), "V6", "V9");
		g.addEdge(edgeFactory.create(), "V4", "V10");

		TreeLayout layout = new TreeLayout<String,Integer>(g);


		VisualizationViewer<String,Integer> vv = new VisualizationViewer<String,Integer>(
				layout);
		vv.setPreferredSize(new Dimension(300, 300));
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<String>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<Integer>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		//the following is just optimization for paper print
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Transformer<String, Paint>() {
					public Paint transform(String p) {
						return Color.YELLOW;
					}
				});

		// mouse
		
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] a) {
		new Ex3();
	}

}
