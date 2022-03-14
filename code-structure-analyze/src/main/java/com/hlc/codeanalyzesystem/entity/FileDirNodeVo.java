package com.hlc.codeanalyzesystem.entity;

import java.util.List;

public class FileDirNodeVo {
    private Integer id;
    private Integer pid;
    private String nodeShortName;
    private String nodePath;
    private List<FileDirNodeVo> children;

    public FileDirNodeVo(Integer id, Integer pid, String nodeShortName, String nodePath) {
        this.id = id;
        this.pid = pid;
        this.nodeShortName = nodeShortName;
        this.nodePath = nodePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getNodeShortName() {
        return nodeShortName;
    }

    public void setNodeShortName(String nodeShortName) {
        this.nodeShortName = nodeShortName;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public List<FileDirNodeVo> getChildren() {
        return children;
    }

    public void setChildren(List<FileDirNodeVo> children) {
        this.children = children;
    }
}
