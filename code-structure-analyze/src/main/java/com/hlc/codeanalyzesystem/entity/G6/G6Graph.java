package com.hlc.codeanalyzesystem.entity.G6;

import java.util.List;

public class G6Graph {
    List<G6Node> nodes;
    List<G6Edge> edges;

    public G6Graph(List<G6Node> nodes, List<G6Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<G6Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<G6Node> nodes) {
        this.nodes = nodes;
    }

    public List<G6Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<G6Edge> edges) {
        this.edges = edges;
    }
}
