package com.hlc.codeanalyzesystem.entity;

import java.util.ArrayList;
import java.util.List;

public class ProjectResultVo {
    private Integer id;

    private Integer linecount;

    private Integer filecount;

    private Integer classcount;

    private Integer functioncount;

    private String longestfile;

    private String longestclass;

    private String longestfunction;

    private String mostComplexFile;

    private List<FileDirNodeVo> projectdir;

    private Double maxComplexity;

    private String projectComplexity;



    public ProjectResultVo() {
        projectdir = new ArrayList<>();
    }


    public String getProjectComplexity() {
        return projectComplexity;
    }

    public void setProjectComplexity(String projectComplexity) {
        this.projectComplexity = projectComplexity;
    }

    public Double getMaxComplexity() {
        return maxComplexity;
    }

    public void setMaxComplexity(Double maxComplexity) {
        this.maxComplexity = maxComplexity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLinecount() {
        return linecount;
    }

    public void setLinecount(Integer linecount) {
        this.linecount = linecount;
    }

    public Integer getFilecount() {
        return filecount;
    }

    public void setFilecount(Integer filecount) {
        this.filecount = filecount;
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

    public String getLongestfile() {
        return longestfile;
    }

    public void setLongestfile(String longestfile) {
        this.longestfile = longestfile;
    }

    public String getLongestclass() {
        return longestclass;
    }

    public void setLongestclass(String longestclass) {
        this.longestclass = longestclass;
    }

    public String getLongestfunction() {
        return longestfunction;
    }

    public void setLongestfunction(String longestfunction) {
        this.longestfunction = longestfunction;
    }

    public String getMostComplexFile() {
        return mostComplexFile;
    }

    public void setMostComplexFile(String mostComplexFile) {
        this.mostComplexFile = mostComplexFile;
    }

    public List<FileDirNodeVo> getProjectdir() {
        return projectdir;
    }

    public void setProjectdir(List<FileDirNodeVo> projectdir) {
        this.projectdir = projectdir;
    }
}
