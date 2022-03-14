package com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.visualization.VisualizationViewer;

public class VertexPopupMenu {
	static JPopupMenu m = new JPopupMenu();

	public static JPopupMenu getVertexPopupMenu() {
		return m;
	}

	public static JPopupMenu update(final Idea v,
			final VisualizationViewer<Idea, Link> vv, Point2D point) {

		m.removeAll();

		// "delete" menu
		String title = v.title;
		JMenuItem mi = new JMenuItem("Delete [" + title + "]");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vv.getGraphLayout().getGraph().removeVertex(v);
				vv.repaint();
			}
		});
		m.add(mi);

		// "schedule" checkbox menu
		final JCheckBoxMenuItem mic = new JCheckBoxMenuItem("Schedule");
		mic.setSelected(v.isSchedule);
		mic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				v.isSchedule = mic.isSelected();
			}
		});
		m.add(mic);

		return m;
	}
}
