package com.hlc.codeanalyzesystem.entity;

import com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead.HalsteadMetrics;

import java.util.List;

public class FunctionVarList {
    private int id;
    private String name;
    private List<String> varList;
    private HalsteadMetrics halsteadMetrics;

    public FunctionVarList() {
    }

    public FunctionVarList(int id, String name, List<String> varList, HalsteadMetrics halsteadMetrics) {
        this.id = id;
        this.name = name;
        this.varList = varList;
        this.halsteadMetrics = halsteadMetrics;
    }

    public FunctionVarList(int id, String name, List<String> varList) {
        this.id = id;
        this.name = name;
        this.varList = varList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getVarList() {
        return varList;
    }

    public void setVarList(List<String> varList) {
        this.varList = varList;
    }

    public HalsteadMetrics getHalsteadMetrics() {
        return halsteadMetrics;
    }

    public void setHalsteadMetrics(HalsteadMetrics halsteadMetrics) {
        this.halsteadMetrics = halsteadMetrics;
    }
}
