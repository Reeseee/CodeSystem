package com.hlc.codeanalyzesystem.entity;

import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeVo {
    private Integer id;
    private Integer pid;
    private String nodeMessage;

    private List<TreeNodeVo> children;

    public TreeNodeVo() {
    }

    public TreeNodeVo(Integer id, Integer pid, String nodeMessage) {
        this.id = id;
        this.pid = pid;
        this.nodeMessage = nodeMessage;
        children = new ArrayList<>();
    }

    public List<TreeNodeVo> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeVo> children) {
        this.children = children;
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

    public String getNodeMessage() {
        return nodeMessage;
    }

    public void setNodeMessage(String nodeMessage) {
        this.nodeMessage = nodeMessage;
    }
}