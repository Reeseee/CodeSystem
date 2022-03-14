package com.hlc.codeanalyzesystem.entity;

public class FileResultVo {

    private Integer id;

    private String name;

    private Integer belongto;


    private Integer linecount;


    private Integer classcount;


    private Integer functioncount;


    private Integer variablecount;


    private String longestclass;


    private String longestfunction;


    private String classlist;


    private String functionlist;


    private String variablelist;


    private String functionvariable;

    private String fileHalstead;

    public FileResultVo() {
    }

    public String getFileHalstead() {
        return fileHalstead;
    }

    public void setFileHalstead(String fileHalstead) {
        this.fileHalstead = fileHalstead;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }


    public Integer getBelongto() {
        return belongto;
    }


    public void setBelongto(Integer belongto) {
        this.belongto = belongto;
    }

    public Integer getLinecount() {
        return linecount;
    }

    public void setLinecount(Integer linecount) {
        this.linecount = linecount;
    }

    public Integer getClasscount() {
        return classcount;
    }

    public void setClasscount(Integer classcount) {
        this.classcount = classcount;
    }

    public Integer getFunctioncount() {
        return functioncount;
    }

    public void setFunctioncount(Integer functioncount) {
        this.functioncount = functioncount;
    }

    public Integer getVariablecount() {
        return variablecount;
    }

    public void setVariablecount(Integer variablecount) {
        this.variablecount = variablecount;
    }

    public String getLongestclass() {
        return longestclass;
    }

    public void setLongestclass(String longestclass) {
        this.longestclass = longestclass == null ? null : longestclass.trim();
    }

    public String getLongestfunction() {
        return longestfunction;
    }

    public void setLongestfunction(String longestfunction) {
        this.longestfunction = longestfunction == null ? null : longestfunction.trim();
    }

    public String getClasslist() {
        return classlist;
    }

    public void setClasslist(String classlist) {
        this.classlist = classlist == null ? null : classlist.trim();
    }

    public String getFunctionlist() {
        return functionlist;
    }

    public void setFunctionlist(String functionlist) {
        this.functionlist = functionlist == null ? null : functionlist.trim();
    }

    public String getVariablelist() {
        return variablelist;
    }

    public void setVariablelist(String variablelist) {
        this.variablelist = variablelist == null ? null : variablelist.trim();
    }

    public String getFunctionvariable() {
        return functionvariable;
    }


    public void setFunctionvariable(String functionvariable) {
        this.functionvariable = functionvariable == null ? null : functionvariable.trim();
    }
}
