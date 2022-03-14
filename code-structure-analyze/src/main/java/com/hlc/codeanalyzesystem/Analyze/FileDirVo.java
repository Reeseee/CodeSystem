package com.hlc.codeanalyzesystem.Analyze;
public class FileDirVo {

    String fullName;

    String shortName;

    int depth;

    public FileDirVo(String fullName, String shortName,int depth) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.depth = depth;
    }


    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "FileDirVo{" +
                "fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", depth=" + depth +
                '}';
    }
}
