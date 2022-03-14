package com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap;

import java.util.Vector;

import org.apache.commons.collections15.Factory;

/**
 * Represents one link to another idea, with a title text and (optionally) a
 * brief.
 */
public class Link {
	static long _id = 0;// seed for generating id
	long id;// unique id
	Idea toIdea;
	String title, brief;

	public Link(Idea toIdea, String title, String brief) {
		this.id = _id++;
		this.toIdea = toIdea;
		this.title = title;
		this.brief = brief;
	}

	public String toString() {
		return title;
	}

}

class LinkFactory implements Factory<Link> {
	static LinkFactory instance = new LinkFactory();

	public Link create() {
		return new Link(new Idea("", "", new Vector<String>()), "", "");
	}

	public static LinkFactory getInstance() {
		return instance;
	}

}