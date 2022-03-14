package com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap;

import java.util.Vector;

import org.apache.commons.collections15.Factory;

/**
 * Represents one link to another idea, with a title text and (optionally) a
 * brief.
 */
public class newLink {
    static long _id = 0;// seed for generating id
    long id;// unique id
    Idea toIdea;
    String title, brief;

    public newLink( String title, String brief) {
        this.id = _id++;
        this.toIdea = toIdea;
        this.title = title;
        this.brief = brief;
    }

    public String toString() {
        return title;
    }

}

class newLinkFactory implements Factory<newLink> {
    static newLinkFactory instance = new newLinkFactory();

    public newLink create() {
        return new newLink("", "");
    }

    public static newLinkFactory getInstance() {
        return instance;
    }

}
