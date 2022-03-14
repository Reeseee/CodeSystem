package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;

public class ComplexGraph {
	SparseGraph<People, Link> g;
	BasicVisualizationServer<People, Link> vv;
	People p1,p2,p3,p4;
	Transformer<People, Paint> vertexPaint;
	Transformer<Link, Stroke> edgeStrokeTransformer;
	JFrame frame;

	public ComplexGraph() {
		createWindow();
		createGraph();
		createTransformers();
		showGraph();
	}

	public void createTransformers() {
		// Setup up a new vertex to paint transformer...
		vertexPaint = new Transformer<People, Paint>() {
			public Paint transform(People p) {
				if (p.equals(p1))
					return Color.GREEN;
				else
					return Color.YELLOW;
			}
		};
		// Set up a new stroke Transformer for the edges
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		edgeStrokeTransformer = new Transformer<Link, Stroke>() {
			public Stroke transform(Link link) {
				return edgeStroke;
			}
		};

	}

	public void showGraph() {

		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<People, Link> layout = new CircleLayout<People, Link>(g);
		layout.setSize(new Dimension(200, 200)); // sets the initial size of the
		// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		vv = new BasicVisualizationServer<People, Link>(layout);
		vv.setPreferredSize(new Dimension(220, 220)); // Sets the viewing area
		// size

		// apply transformers
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		//vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<People>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<Link>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

	public void createGraph() {
		// Graph<V, E> where V is the type of the vertices and E is the type of
		// the edges
		g = new SparseGraph<People, Link>();
		// Add some vertices
		 p1=new People("Sam");
		 p2=new People("Kate");
		 p3=new People("John");
		 p4=new People("Wang");
		g.addVertex(p1);
		g.addVertex(p2);
		g.addVertex(p3);
		g.addVertex(p4);
		// Add some edges. default is for undirected edges.
		Link l1=new Link("Email");
		Link l2=new Link("Telephone");
		Link l3=new Link("Interview");
		Link l4=new Link("Letter");
		g.addEdge(l1, p1, p2);
		g.addEdge(l2, p1, p3);
		g.addEdge(l3, p2, p3);
		g.addEdge(l4, p3, p4);
	}

	public void createWindow() {
		frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] a) {
		new ComplexGraph();
	}
}

class People {
	static int sid = 0;
	int id;
	String name;

	People(String name) {
		id = ++sid;
		this.name = name;
	}

	public String toString() {
		return name + " - " + id;
	}
}

class Link {
	static int sid = 0;
	int id;
	String contact;

	Link(String contact) {
		id = ++sid;
		this.contact = contact;
	}

	public String toString(){
		return contact+"("+id+")";
	}
}