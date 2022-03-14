package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.Dimension;
import javax.swing.JFrame;

public class GraphView {
	SparseGraph<String, String> g;
	JFrame frame;

	public GraphView() {
		createWindow();
		createGraph();
		showGraph();
	}

	public void showGraph() {

		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<String, String> layout = new CircleLayout(g);
		layout.setSize(new Dimension(150, 150)); // sets the initial size of the
													// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(
				layout);
		vv.setPreferredSize(new Dimension(160, 160)); // Sets the viewing area
														// size

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
		new GraphView();
	}
}
