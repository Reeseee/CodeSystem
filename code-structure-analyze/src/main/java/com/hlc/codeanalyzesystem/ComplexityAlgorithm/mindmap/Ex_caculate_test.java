package com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.scoring.BarycenterScorer;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import main.java.Dao.returnObject;
import main.java.method.generalResult;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;


@SuppressWarnings("unused")
public class Ex_caculate_test {

	@SuppressWarnings("null")
	public Ex_caculate_test() {
		SparseGraph<Idea, Link> g = new SparseGraph<Idea, Link>();
		LinkedList<Idea> vertex=new LinkedList<Idea>();     //创建链表保存顶点
		LinkedList<Link>  LinkList = new LinkedList<Link>();     //创建链表保存边

		generalResult result=new generalResult();      //构造反序列对象
		returnObject ro=result.getReturnObject("C:\\Download\\test\\cassandra.xml");
		List<ArrayList<String>> classname=ro.getClassName();      //获得类名序列
		ListIterator<ArrayList<String>> classnameListIterator=classname.listIterator();
		ArrayList<String> classnameFlag;
		LinkedList<String> noSameName=new LinkedList<String>();
		boolean exist,flag;


		ArrayList<String> test=new ArrayList<String>();
		test.add("pig");
		System.out.print(test.get(0));    //输出测试
		while(classnameListIterator.hasNext()){
			classnameFlag=classnameListIterator.next();
			String first=classnameFlag.get(0);
			String second=classnameFlag.get(1);
			exist=false;
			for(String str:noSameName){      //判断第一个元素
				if(first==str)  exist=true;
			}
			if(!exist) noSameName.add(first);
			exist=false;
			for(String str:noSameName){      //判断第二个元素
				if(second==str)  exist=true;
			}
			if(!exist) noSameName.add(second);
		}
//		System.out.print(noSameName);    //测试noSameName
		for(String str:noSameName){
			vertex.add(new Idea(str, "", null));
		}
		for (Idea v : vertex) {              //所有顶点加入图中
			g.addVertex(v);}

		for(ArrayList<String> a:classname) {
			String f1=a.get(0);
			String f2=a.get(1);
			for (Idea v : vertex) {              //往图中加入所有边
				v.addLink(new Link(v, "", "")); //创建新的边对象
				if(v.toString()==f1){
					for (Idea ov : vertex) {                //建立关系的顶点的边
						flag=false;
						if (ov != v) {
							if (ov.toString() ==f2) flag=true; }
						if(flag) g.addEdge(new Link(v, "", ""),v,ov);
					}
				}
			}
		}


		// The Layout<V, E> is parameterized by the vertex and edge types
		FRLayout<Idea, Link> layout = new FRLayout<Idea, Link>(g);

		layout.setSize(new Dimension(600, 600)); // sets the initial size of the
		VisualizationViewer<Idea, Link> vv = new VisualizationViewer<Idea, Link>(
				layout);
		vv.setPreferredSize(new Dimension(720, 720));
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Idea>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<Link>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);





		// //// Manually set RoundRectangle vertex shape ! ////////
		Transformer<Idea, Shape>vstr = new Transformer<Idea, Shape>() {
			public Shape transform(Idea i) {
				int len = i.toString().length();
				if (len < 4)
					len = 4;

				RoundRectangle2D.Double r = new RoundRectangle2D.Double(
						-len * 10, -20, len * 20,20, 10, 10);
				return r;
			}
		};
		vv.getRenderContext().setVertexShapeTransformer(vstr);



		//the following is just optimization for paper print
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Transformer<Idea, Paint>() {
					public Paint transform(Idea p) {
						return Color.GREEN;
					}
				});

		vv.getRenderContext().setVertexFontTransformer(
				new Transformer<Idea, Font>() {
					public Font transform(Idea i) {
						return new Font("宋体", Font.PLAIN, 20);
					}
				});
		BetweennessCentrality b=new BetweennessCentrality(g);
		BarycenterScorer  bcs=new BarycenterScorer(g);
		ClosenessCentrality cc=new ClosenessCentrality(g);
		DegreeScorer ds=new DegreeScorer(g);
//		System.out.print("此节点的中心度为"+b.getEdgeScore(l23)+"\n");
//		System.out.println("计算BarycenterScorer："+bcs.getVertexScore(id1));
//		System.out.print("计算ClosenessCentrality："+cc.getVertexScore(id2));
		for(Idea v : vertex) {
//			System.out.print("此节点"+i+"的中心度为"+bcs.getVertexScore(v[i])+"\n");
//			System.out.print("此节点"+i+"的BarycenterScorer中心度为"+bcs.getVertexScore(v[i])+"\n");
			System.out.print(v+":  "+ds.getVertexScore(v)+"\n");
		}
		// mouse plugin demo
		EditingModalGraphMouse<Idea, Link> gm = new EditingModalGraphMouse<Idea, Link>(      //下面部分为可视化
				vv.getRenderContext(), IdeaFactory.getInstance(), LinkFactory
				.getInstance());
		PopupVertexEdgeMenuMousePlugin0<Idea, Link> myPlugin = new PopupVertexEdgeMenuMousePlugin0<Idea, Link>();
		JPopupMenu edgeMenu = new JPopupMenu("Vertex Menu");
		JPopupMenu vertexMenu = new JPopupMenu("Edge Menu");
		edgeMenu.add(new JMenuItem("edge!"));
		vertexMenu.add(new JMenuItem("vertex!"));
		myPlugin.setEdgePopup(edgeMenu);
		myPlugin.setVertexPopup(vertexMenu);
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


	public static List<List<Double>> mmCalculate(List<ArrayList<String>> classname){
		SparseGraph<Idea, Link> g = new SparseGraph<Idea, Link>();
		LinkedList<Idea> vertex=new LinkedList<Idea>();     //创建链表保存顶点
		LinkedList<Link>  LinkList = new LinkedList<Link>();     //创建链表保存边

		//generalResult result=new generalResult();      //构造反序列对象
		//returnObject ro=result.getReturnObject(path);
		//List<ArrayList<String>> classname=ro.getClassName();      //获得类名序列
		ListIterator<ArrayList<String>> classnameListIterator=classname.listIterator();
		ArrayList<String> classnameFlag;
		LinkedList<String> noSameName=new LinkedList<String>();
		boolean exist,flag;


		while(classnameListIterator.hasNext()){
			classnameFlag=classnameListIterator.next();
			String first=classnameFlag.get(0);
			String second=classnameFlag.get(1);
			exist=false;
			for(String str:noSameName){      //判断第一个元素
				if(first==str)  exist=true;
			}
			if(!exist) noSameName.add(first);
			exist=false;
			for(String str:noSameName){      //判断第二个元素
				if(second==str)  exist=true;
			}
			if(!exist) noSameName.add(second);
		}
//		System.out.print(noSameName);    //测试noSameName
		for(String str:noSameName){
			vertex.add(new Idea(str, "", null));
		}
		for (Idea v : vertex) {              //所有顶点加入图中
			g.addVertex(v);}

		for(ArrayList<String> a:classname) {
			String f1=a.get(0);
			String f2=a.get(1);
			for (Idea v : vertex) {              //往图中加入所有边
				v.addLink(new Link(v, "", "")); //创建新的边对象
				if(v.toString()==f1){
					for (Idea ov : vertex) {                //建立关系的顶点的边
						flag=false;
						if (ov != v) {
							if (ov.toString() ==f2) flag=true; }
						if(flag) g.addEdge(new Link(v, "", ""),v,ov);
					}
				}
			}
		}


		// The Layout<V, E> is parameterized by the vertex and edge types
		FRLayout<Idea, Link> layout = new FRLayout<Idea, Link>(g);

		layout.setSize(new Dimension(600, 600)); // sets the initial size of the
		VisualizationViewer<Idea, Link> vv = new VisualizationViewer<Idea, Link>(
				layout);
		vv.setPreferredSize(new Dimension(720, 720));
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<Idea>());
		vv.getRenderContext().setEdgeLabelTransformer(
				new ToStringLabeller<Link>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);





		// //// Manually set RoundRectangle vertex shape ! ////////
		Transformer<Idea, Shape>vstr = new Transformer<Idea, Shape>() {
			public Shape transform(Idea i) {
				int len = i.toString().length();
				if (len < 4)
					len = 4;

				RoundRectangle2D.Double r = new RoundRectangle2D.Double(
						-len * 10, -20, len * 20,20, 10, 10);
				return r;
			}
		};
		vv.getRenderContext().setVertexShapeTransformer(vstr);



		//the following is just optimization for paper print
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Transformer<Idea, Paint>() {
					public Paint transform(Idea p) {
						return Color.GREEN;
					}
				});

		vv.getRenderContext().setVertexFontTransformer(
				new Transformer<Idea, Font>() {
					public Font transform(Idea i) {
						return new Font("宋体", Font.PLAIN, 20);
					}
				});
		BetweennessCentrality b=new BetweennessCentrality(g);
		BarycenterScorer  bcs=new BarycenterScorer(g);
		ClosenessCentrality cc=new ClosenessCentrality(g);
		DegreeScorer ds=new DegreeScorer(g);
//		System.out.print("此节点的中心度为"+b.getEdgeScore(l23)+"\n");
//		System.out.println("计算BarycenterScorer："+bcs.getVertexScore(id1));
//		System.out.print("计算ClosenessCentrality："+cc.getVertexScore(id2));
		List<List<Double>> res = new ArrayList<>();
//		Map<String,Integer> complex = new HashMap<>();
		for(Idea v : vertex) {
//			System.out.print("此节点"+i+"的中心度为"+bcs.getVertexScore(v[i])+"\n");
//			System.out.print("此节点"+i+"的BarycenterScorer中心度为"+bcs.getVertexScore(v[i])+"\n");
//			complex.put(v.title,ds.getVertexScore(v));
//			System.out.print(v+":  "+ds.getVertexScore(v)+"\n");
			List<Double> temp = new ArrayList<>();
			temp.add(ds.getVertexScore(v).doubleValue());
			temp.add(bcs.getVertexScore(v));
			res.add(temp);
		}
		return res;
	}

	public static void main(String[] a) {
		new Ex_caculate_test();
	}

}

