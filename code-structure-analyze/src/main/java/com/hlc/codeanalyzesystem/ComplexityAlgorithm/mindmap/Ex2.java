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

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.*;
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

public class Ex2 {
    Ex A;
	public Ex2() {
		SparseGraph<Idea, Link> g = new SparseGraph<Idea, Link>();
		Idea id1 = new Idea("mind", "", null);
		Idea id2 = new Idea("idea", "", null);
		Idea id3 = new Idea("intellect", "", null);
		Idea id4 = new Idea("heed", "", null);
		Idea id5 = new Idea("beware", "", null);
		Idea id6 = new Idea("judgement", "", null);
		Idea id7 = new Idea("think", "", null);

		Link l12 = new Link(id2, "create", "");
		id1.addLink(l12);

		Link l23 = new Link(id3, "clever", "");
		id2.addLink(l23);

		Link l24 = new Link(id4, "understand?", "");
		id2.addLink(l24);

		Link l15 = new Link(id5, "knowing", "");
		id2.addLink(l15);

		Link l16 = new Link(id6, "right or wrong", "");
		id2.addLink(l16);

		Link l17 = new Link(id7, "function", "");
		id2.addLink(l17);

		g.addVertex(id1);
		g.addVertex(id2);
		g.addVertex(id3);
		g.addVertex(id4);
		g.addVertex(id5);
		g.addVertex(id6);
		g.addVertex(id7);

		g.addEdge(l12, id1, id2);
		g.addEdge(l23, id2, id3);
		g.addEdge(l24, id2, id4);
		g.addEdge(l15, id1, id5);
		g.addEdge(l16, id1, id6);
		g.addEdge(l17, id1, id7);

		// The Layout<V, E> is parameterized by the vertex and edge types
		FRLayout<Idea, Link> layout = new FRLayout<Idea, Link>(g);

		layout.setSize(new Dimension(300, 300)); // sets the initial size of the
		VisualizationViewer<Idea, Link> vv = new VisualizationViewer<Idea, Link>(
				layout);
		vv.setPreferredSize(new Dimension(320, 320));
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Idea>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<Link>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		
		/*
		//vertex label renderer with icon & html
		DefaultVertexLabelRenderer vlr = new DefaultVertexLabelRenderer(Color.BLUE){
			public <Idea> Component getVertexLabelRendererComponent(JComponent vv, Object value,
		            Font font, boolean isSelected, Idea vertex) {
				super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
				setIcon(new ImageIcon("shy.jpg"));
				this.setBorder(BorderFactory.createEtchedBorder());
				setText("<html>title:<br><b>"+vertex.toString()+"</b></html>");
				return this;
			}
		};
		vv.getRenderContext().setVertexLabelRenderer(vlr);
		*/
		
		

		// //// Manually set RoundRectangle vertex shape ! ////////
		Transformer<Idea, Shape>vstr = new Transformer<Idea, Shape>() {
			public Shape transform(Idea i) {
				int len = i.toString().length();
				if (len < 4)
					len = 4;
				
				RoundRectangle2D.Double r = new RoundRectangle2D.Double(
						-len * 5, -10, len * 10, 20, 10, 10);
				return r;
			}
		};
		vv.getRenderContext().setVertexShapeTransformer(vstr);

		

		//the following is just optimization for paper print
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Transformer<Idea, Paint>() {
					public Paint transform(Idea p) {
						return Color.YELLOW;
					}
				});

		vv.getRenderContext().setVertexFontTransformer(
				new Transformer<Idea, Font>() {
					public Font transform(Idea i) {
						return new Font("����", Font.PLAIN, 16);
					}
				});

		// mouse plugin demo
		EditingModalGraphMouse<Idea, Link> gm = new EditingModalGraphMouse<Idea, Link>(
				vv.getRenderContext(), IdeaFactory.getInstance(), LinkFactory
						.getInstance());
		PopupVertexEdgeMenuMousePlugin<Idea, Link> myPlugin = new PopupVertexEdgeMenuMousePlugin<Idea, Link>();
		// Removes the existing popup editing plugin!
		gm.remove(gm.getPopupEditingPlugin());
		// Add our new plugin to the mouse
		gm.add(myPlugin);
		vv.setGraphMouse(gm);
		
		
		
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] a) {
		new Ex2();
	}

}
