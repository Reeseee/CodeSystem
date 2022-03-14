package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

public class Kneighbor {
	static int edgeCount = 0; // This works with the inner MyEdge class
	// since inner classes cannot have static members
	DirectedGraph<MyNode, MyLink> g;
	MyNode n1, n2, n3, n4, n5;

	/** Creates a new instance of BasicDirectedGraph */
	public Kneighbor() {
	}

	/** Constructs an example directed graph with our vertex and edge classes */
	public void constructGraph() {
		g = new DirectedSparseMultigraph<MyNode, MyLink>();
		// Create some MyNode objects to use as vertices
		n1 = new MyNode(1);
		n2 = new MyNode(2);
		n3 = new MyNode(3);
		n4 = new MyNode(4);
		n5 = new MyNode(5); // note n1-n5 declared elsewhere.
		// Add some directed edges along with the vertices to the graph
		g.addEdge(new MyLink(2.0, 48), n1, n2, EdgeType.DIRECTED); // This
		// method
		g.addEdge(new MyLink(2.0, 48), n2, n3, EdgeType.DIRECTED);
		g.addEdge(new MyLink(3.0, 192), n3, n5, EdgeType.DIRECTED);
		g.addEdge(new MyLink(2.0, 48), n5, n4, EdgeType.DIRECTED); // or we can
		// use
		g.addEdge(new MyLink(2.0, 48), n4, n2); // In a directed graph the
		g.addEdge(new MyLink(2.0, 48), n3, n1); // first node is the source
		g.addEdge(new MyLink(10.0, 48), n2, n5);// and the second the
		// destination
	}

	public void calcUnweightedShortestPath() {
		DijkstraShortestPath<MyNode, MyLink> alg = new DijkstraShortestPath(g);
		List<MyLink> l = alg.getPath(n1, n4);
		System.out.println("The shortest unweighted path from " + n1 + " to "
				+ n4 + " is:");
		System.out.println(l.toString());
	}

	public void calcWeightedShortestPath() {
		Transformer<MyLink, Double> wtTransformer = new Transformer<MyLink, Double>() {
			public Double transform(MyLink link) {
				return link.weight;
			}
		};
		DijkstraShortestPath<MyNode, MyLink> alg = new DijkstraShortestPath(g,
				wtTransformer);
		List<MyLink> l = alg.getPath(n1, n4);
		Number dist = alg.getDistance(n1, n4);
		System.out.println("The shortest weighted path from " + n1 + " to "
				+ n4 + " is:");
		System.out.println(l.toString());
		System.out.println("and the length of the path is: " + dist);
	}

	public void calcMaxFlow() {
		// For the Edmonds-Karp Max Flow algorithm we have the following
		// parameters: the graph, source vertex, sink vertex, transformer of
		// edge capacities,
		// map of edge flow values.

		Transformer<MyLink, Double> capTransformer = new Transformer<MyLink, Double>() {
			public Double transform(MyLink link) {
				return link.capacity;
			}
		};
		Map<MyLink, Double> edgeFlowMap = new HashMap<MyLink, Double>();
		// This Factory produces new edges for use by the algorithm
		Factory<MyLink> edgeFactory = new Factory<MyLink>() {
			public MyLink create() {
				return new MyLink(1.0, 1.0);
			}
		};

		EdmondsKarpMaxFlow<MyNode, MyLink> alg = new EdmondsKarpMaxFlow(g, n2,
				n5, capTransformer, edgeFlowMap, edgeFactory);
		alg.evaluate(); // If you forget this you won't get an error but you
		// will get a wrong answer!!!
		System.out.println("The max flow is: " + alg.getMaxFlow());
		System.out.println("The edge set is: "
				+ alg.getMinCutEdges().toString());
	}

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
				return new SparseGraph();
			}
		};
	}

	public static void main(String[] args) {
		Kneighbor myApp = new Kneighbor();
		myApp.createFactories();

		MatrixFile mf = new MatrixFile(null, myApp.graphFactory,
				myApp.vertexFactory, myApp.edgeFactory);
		Graph g = mf.load("c:/g.txt");
		System.out.println(g);

		Layout<Integer, String> layout = new CircleLayout(g);
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Integer>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<String>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame frame = new JFrame();
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

		if (true)
			return;

		myApp.constructGraph();
		System.out.println(myApp.g.toString());
		myApp.calcUnweightedShortestPath();
		myApp.calcWeightedShortestPath();
		myApp.calcMaxFlow();
	}

	class MyNode {
		int id;

		public MyNode(int id) {
			this.id = id;
		}

		public String toString() {
			return "V" + id;
		}
	}

	class MyLink {
		double capacity;
		double weight;
		int id;

		public MyLink(double weight, double capacity) {
			this.id = edgeCount++;
			this.weight = weight;
			this.capacity = capacity;
		}

		public String toString() {
			return "E" + id;
		}

	}

}
