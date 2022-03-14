package com.hlc.codeanalyzesystem.entity;

import java.util.HashSet;
import java.util.Set;

public class FileDependencyVo {
    Set<String> dependency;
    Set<String> beDependentOn;

    public FileDependencyVo() {
        this.dependency = new HashSet<>();
        this.beDependentOn = new HashSet<>();
    }

    public FileDependencyVo(Set<String> dependency, Set<String> beDependentOn) {
        this.dependency = dependency;
        this.beDependentOn = beDependentOn;
    }

    public Set<String> getDependency() {
        return dependency;
    }

    public void setDependency(Set<String> dependency) {
        this.dependency = dependency;
    }

    public Set<String> getBeDependentOn() {
        return beDependentOn;
    }

    public void setBeDependentOn(Set<String> beDependentOn) {
        this.beDependentOn = beDependentOn;
    }
}
