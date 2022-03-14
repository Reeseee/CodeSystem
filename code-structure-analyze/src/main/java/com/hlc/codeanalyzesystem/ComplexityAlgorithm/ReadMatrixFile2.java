package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.MatrixFile;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

public class ReadMatrixFile2 {

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
	// /////////
	Hashtable<String, Number> eWeights = new Hashtable<String, Number>();

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
		final ReadMatrixFile2 myApp = new ReadMatrixFile2();
		myApp.createFactories();

//		MatrixFile<Integer, String> mf = new MatrixFile<Integer, String>(
//				myApp.eWeights, myApp.graphFactory, myApp.vertexFactory,
//				myApp.edgeFactory);
//		Graph<Integer, String> g = mf.load("c:/g2.txt");
		
		Graph<Integer, String> g;
		try {
			BufferedReader reader =
				new BufferedReader(new FileReader("c:/g2.txt"));
			DoubleMatrix2D matrix = createMatrixFromFile(reader);//!!!
			g = GraphMatrixOperations.<Integer,String>matrixToGraph(matrix,
		    		myApp.graphFactory, myApp.vertexFactory, myApp.edgeFactory,myApp.eWeights);//!!!
			reader.close();
		} catch (IOException ioe) {
			throw new RuntimeException("Error in loading file " + "c:/g2.txt", ioe);
		}
		
		
		
		
		
		System.out.println(g);

		
		// converts a string (of an edge) to the edge's weight
		Transformer<String, Double> nev = new Transformer<String, Double>() {
			public Double transform(String s) {
				Number v = (Number) myApp.eWeights.get(s);
//				System.out.println(s);
				if (v != null){
//					System.out.println(v);
					return v.doubleValue();
				}
				else
					return 0.0;
			}
		};

		DijkstraShortestPath<Integer, String> alg = new DijkstraShortestPath<Integer, String>(
				g, nev);
		final List<String> l = alg.getPath(new Integer(0), new Integer(9));
		System.out.println("The shortest unweighted path from 0 to 9 is:");
		System.out.println(l.toString());
		Number dist = alg.getDistance(new Integer(0), new Integer(9));
		System.out.println("and the length of the path is: " + dist);

		Layout<Integer, String> layout = new CircleLayout(g);
		layout.setSize(new Dimension(290, 290));
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Integer>());
		vv.getRenderContext().setEdgeFontTransformer(new Transformer(){

			public Object transform(Object arg0) {
				return new Font("����", Font.ITALIC, 24);
			}
			
		});
		
		Transformer<String, String> ev = new Transformer<String, String>(){
			public String transform(String s) {
				Number v = (Number) myApp.eWeights.get(s);
				if (v != null){
					return "----"+v.intValue();
				}
				else
					return "";
			}			
		};
		vv.getRenderContext().setEdgeLabelTransformer(ev);
		vv.getRenderContext().setLabelOffset(30);
		
		
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

	
	
	
	////////////////////////////
	private static DoubleMatrix2D createMatrixFromFile(BufferedReader reader)
	throws IOException
{
	List<List<Double>> rows = new ArrayList<List<Double>>();
	String currentLine = null;
	while ((currentLine = reader.readLine()) != null) {
		StringTokenizer tokenizer = new StringTokenizer(currentLine);
		if (tokenizer.countTokens() == 0) {
			break;
		}
		List<Double> currentRow = new ArrayList<Double>();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			currentRow.add(Double.parseDouble(token));
		}
		rows.add(currentRow);
	}
	int size = rows.size();
	DoubleMatrix2D matrix = new SparseDoubleMatrix2D(size, size);
	for (int i = 0; i < size; i++) {
		List<Double> currentRow = rows.get(i);
		if (currentRow.size() != size) {
			throw new IllegalArgumentException(
				"Matrix must have the same number of rows as columns");
		}
		for (int j = 0; j < size; j++) {
			double currentVal = currentRow.get(j);
			if (currentVal != 0) {
				matrix.setQuick(i, j, currentVal);
			}
		}
	}
	return matrix;
}

	
}
