package com.hlc.codeanalyzesystem.Analyze;

public class NamePosPair {
    String name;

    int pos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public NamePosPair(String name, int pos) {
        this.name = name;
        this.pos = pos;
    }
}
