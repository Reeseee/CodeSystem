package com.hlc.codeanalyzesystem.entity;

import java.io.Serializable;
import java.util.Map;

public class Graph implements Serializable {
    int [][]graph;
    Map<Integer,String> idNameMapping;
    Map<String,Integer> nameIdMapping;

    public Graph() {
    }

    public Graph(int[][] graph, Map<Integer, String> idNameMapping, Map<String, Integer> nameIdMapping) {
        this.graph = graph;
        this.idNameMapping = idNameMapping;
        this.nameIdMapping = nameIdMapping;
    }

    public int[][] getGraph() {
        return graph;
    }

    public void setGraph(int[][] graph) {
        this.graph = graph;
    }

    public Map<Integer, String> getIdNameMapping() {
        return idNameMapping;
    }

    public void setIdNameMapping(Map<Integer, String> idNameMapping) {
        this.idNameMapping = idNameMapping;
    }

    public Map<String, Integer> getNameIdMapping() {
        return nameIdMapping;
    }

    public void setNameIdMapping(Map<String, Integer> nameIdMapping) {
        nameIdMapping = nameIdMapping;
    }
}
