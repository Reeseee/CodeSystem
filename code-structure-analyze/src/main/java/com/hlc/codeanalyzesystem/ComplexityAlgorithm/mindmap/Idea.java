package com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap;

import java.util.Date;
import java.util.Vector;
import org.apache.commons.collections15.Factory;

/**
 * Mind map node: Idea
 * 
 * 
 */
public class Idea {
	static long _id = 0;// seed for generating id
	long id;// unique id of this idea
	String title, brief;// essential data
	Vector<String> keywords;// keywords(tags)
	Vector<Link> links;// pointer to other nodes
	Date date;
	boolean isSchedule = false;
	int importance = 0;

	public Idea(String title, String brief, Vector<String> keywords) {
		this.id = _id++;
		this.title = title;
		this.brief = brief;
		if (keywords == null)
			this.keywords = keywords;
		else
			this.keywords = new Vector<String>();
		this.date = new Date();
		this.links = new Vector<Link>();
		isSchedule = false;
		importance = 0;
	}

	public void addLink(Link link) {
		this.links.add(link);
	}

	public String toString() {
		return title;
	}
}

class IdeaFactory implements Factory<Idea> {
	static IdeaFactory instance = new IdeaFactory();

	public Idea create() {
		return new Idea("", "", new Vector<String>());
	}

	public static IdeaFactory getInstance() {
		return instance;
	}

}