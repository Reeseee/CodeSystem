package com.hlc.codeanalyzesystem.Analyze;

public class VariableInfo {

    String type;

    String name;

    int scope;

    String belongTo;

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariableInfo(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
