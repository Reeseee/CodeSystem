package com.hlc.codeanalyzesystem.ComplexityAlgorithm;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.commons.collections15.Factory;

@SuppressWarnings("unused")
public class EditGraph {
	JFrame frame;

	// declare the graph with specified type of vertex & edge
	SparseGraph<Integer, String> g;
	Factory<Integer> vertexFactory;
	Factory<String> edgeFactory;
	static int vi = 0, ei = 0;// counter for creating vertics & edges

	public EditGraph() {
		createWindow();
		createFactories();
		createGraph();
		showGraph();
	}

	public void showGraph() {

		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Integer, String> layout = new StaticLayout<Integer, String>(g);
		layout.setSize(new Dimension(450, 450)); // sets the initial size
		VisualizationViewer<Integer, String> vv = new VisualizationViewer<Integer, String>(layout);
		vv.setPreferredSize(new Dimension(460, 460)); // Sets the viewing area
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		// **************this is how we can edit the graph!!!
		EditingModalGraphMouse<Integer, String> gm = new EditingModalGraphMouse<Integer, String>(
				vv.getRenderContext(), vertexFactory, edgeFactory);
		vv.setGraphMouse(gm);

		// ***************this is how we can toggle mouse mode
		// Let's add a menu for changing mouse modes
		 JMenuBar menuBar = new JMenuBar();
		 JMenu modeMenu = gm.getModeMenu(); // Obtain mode menu from the mouse
		 modeMenu.setText("Mouse Mode");
		 modeMenu.setIcon(null); // I'm using this in a main menu
		 modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size
		 menuBar.add(modeMenu);
		 frame.setJMenuBar(menuBar);

		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

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
	}

	public void createGraph() {
		g = new SparseGraph<Integer, String>();
		System.out.println(g);
	}

	public void createWindow() {
		frame = new JFrame("Simple Graph Edit");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] a) {
		new EditGraph();
	}
}
