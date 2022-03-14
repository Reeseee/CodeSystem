package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
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

public class GraphView2 {
	SparseGraph<String, String> g;
	BasicVisualizationServer<String, String> vv;
	Transformer<String, Paint> vertexPaint;
	Transformer<String, Stroke> edgeStrokeTransformer;
	JFrame frame;

	public GraphView2() {
		createWindow();
		createGraph();
		createTransformers();
		showGraph();
	}

	public void createTransformers() {
		// Setup up a new vertex to paint transformer...
		vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String s) {
				if (s.equals("John"))
					return Color.GREEN;
				else
					return Color.YELLOW;
			}
		};
		// Set up a new stroke Transformer for the edges
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				return edgeStroke;
			}
		};

	}

	public void showGraph() {

		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<String, String> layout = new CircleLayout(g);
		layout.setSize(new Dimension(150, 150)); // sets the initial size of the
		// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		vv = new BasicVisualizationServer<String, String>(layout);
		vv.setPreferredSize(new Dimension(160, 160)); // Sets the viewing area
		// size

		// apply transformers
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<String>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

	public void createGraph() {
		// Graph<V, E> where V is the type of the vertices and E is the type of
		// the edges
		g = new SparseGraph<String, String>();
		// Add some vertices
		g.addVertex("Sam");
		g.addVertex("Kate");
		g.addVertex("John");
		// Add some edges. default is for undirected edges.
		g.addEdge("Edge-A", "Sam", "Kate");
		g.addEdge("Edge-B", "Kate", "John");
	}

	public void createWindow() {
		frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] a) {
		new GraphView2();
	}
}
