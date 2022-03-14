package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.MatrixFile;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ReadMatrixFile {

	/**
	 * public MatrixFile(Map<E, Number> weightKey, Factory<? extends Graph<V,
	 * E>> graphFactory, Factory<V> vertexFactory, Factory<E> edgeFactory)
	 */

	// declare the graph with specified type of vertex & edge
	SparseGraph<Integer, String> graph;
	Map<String, Number> mWeightKey;// use edge type to parameter the weight key
	// declare the factories with the above specified types
	Factory<SparseGraph<Integer, String>> graphFactory;
	Factory<Integer> vertexFactory;
	Factory<String> edgeFactory;
	static int vi = 0, ei = 0;// counter for creating vertics & edges

	public void createFactories() {
		vertexFactory = new Factory<Integer>() {
			public Integer create() {
				return new Integer(vi++);
			}
		};
		edgeFactory = new Factory<String>() {
			public String create() {
				return "" + ei++;
			}
		};
		graphFactory = new Factory<SparseGraph<Integer, String>>() {
			public SparseGraph<Integer, String> create() {
				return new SparseGraph<Integer, String>();
			}
		};
	}

	public static void main(String[] args) {
		ReadMatrixFile myApp = new ReadMatrixFile();
		myApp.createFactories();

		MatrixFile<Integer, String> mf = new MatrixFile<Integer, String>(null,
				myApp.graphFactory, myApp.vertexFactory, myApp.edgeFactory);
		Graph g = mf.load("c:/g.txt");
		System.out.println(g);

		DijkstraShortestPath<Integer, String> alg = new DijkstraShortestPath<Integer, String>(
				g);
		final List<String> l = alg.getPath(new Integer(0), new Integer(9));
		System.out.println("The shortest unweighted path from 0 to 9 is:");
		System.out.println(l.toString());

		Layout<Integer, String> layout = new CircleLayout(g);
		layout.setSize(new Dimension(290, 290));
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Integer>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<String>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.setPreferredSize(new Dimension(300, 300));

		// Set up stroke Transformers for the edges
		final Stroke edgeStroke = new BasicStroke(1);
		final Stroke shortestStroke = new BasicStroke(4);// thick edge line!
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				for (int i = 0; i < l.size(); i++) {
					if (l.get(i).equals(s))
						return shortestStroke;
				}
				return edgeStroke;
			}
		};
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);

		//you can comment this line: just for better paper-printing
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Transformer<Integer, Paint>() {
					public Paint transform(Integer p) {
						return Color.YELLOW;
					}
				});

		JFrame frame = new JFrame();
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

}
