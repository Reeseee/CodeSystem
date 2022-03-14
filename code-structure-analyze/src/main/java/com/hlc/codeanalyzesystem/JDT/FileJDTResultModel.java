package com.hlc.codeanalyzesystem.JDT;

import com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead.HalsteadMetrics;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.*;

public class FileJDTResultModel {
    List<TypeDeclaration> classes;
    List<MethodDeclaration> functions;
    List<VariableDeclarationFragment> vars;
    Map<MethodDeclaration,List<VariableDeclarationFragment>> localVarMap;
    Map<MethodDeclaration,HalsteadMetrics> MethodComplexityMap;

    private Integer lineCount;

    private Integer classCount;

    private Integer functionCount;

    private Integer variableCount;

    private String longestClass;

    private Integer longestClassLine;

    private String longestFunction;

    private Integer longestFunctionLine;

    private List<Object> imports;

    private String packageName;

    private HalsteadMetrics halsteadMetrics;

    private List<String> calls;


    public List<String> getCalls() {
        return calls;
    }

    public void setCalls(List<String> calls) {
        this.calls = calls;
    }

    public Map<MethodDeclaration, HalsteadMetrics> getMethodComplexityMap() {
        return MethodComplexityMap;
    }

    public void setMethodComplexityMap(Map<MethodDeclaration, HalsteadMetrics> methodComplexityMap) {
        MethodComplexityMap = methodComplexityMap;
    }

    public HalsteadMetrics getHalsteadMetrics() {
        return halsteadMetrics;
    }

    public void setHalsteadMetrics(HalsteadMetrics halsteadMetrics) {
        this.halsteadMetrics = halsteadMetrics;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public FileJDTResultModel(){
        MethodComplexityMap = new HashMap<>();
    }

    public List<Object> getImports() {
        return imports;
    }

    public void setImports(List<Object> imports) {
        this.imports = imports;
    }

    public Integer getLongestClassLine() {
        return longestClassLine;
    }

    public void setLongestClassLine(Integer longestClassLine) {
        this.longestClassLine = longestClassLine;
    }

    public Integer getLongestFunctionLine() {
        return longestFunctionLine;
    }

    public void setLongestFunctionLine(Integer longestFunctionLine) {
        this.longestFunctionLine = longestFunctionLine;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }

    public Integer getClassCount() {
        return classCount;
    }

    public void setClassCount(Integer classCount) {
        this.classCount = classCount;
    }

    public Integer getFunctionCount() {
        return functionCount;
    }

    public void setFunctionCount(Integer functionCount) {
        this.functionCount = functionCount;
    }

    public Integer getVariableCount() {
        return variableCount;
    }

    public void setVariableCount(Integer variableCount) {
        this.variableCount = variableCount;
    }

    public String getLongestClass() {
        return longestClass;
    }

    public void setLongestClass(String longestClass) {
        this.longestClass = longestClass;
    }

    public String getLongestFunction() {
        return longestFunction;
    }

    public void setLongestFunction(String longestFunction) {
        this.longestFunction = longestFunction;
    }

    public List<TypeDeclaration> getClasses() {
        return classes;
    }

    public void setClasses(List<TypeDeclaration> classes) {
        this.classes = classes;
    }

    public List<MethodDeclaration> getFunctions() {
        return functions;
    }

    public void setFunctions(List<MethodDeclaration> functions) {
        this.functions = functions;
    }

    public List<VariableDeclarationFragment> getVars() {
        return vars;
    }

    public void setVars(List<VariableDeclarationFragment> vars) {
        this.vars = vars;
    }

    public Map<MethodDeclaration, List<VariableDeclarationFragment>> getLocalVarMap() {
        return localVarMap;
    }

    public void setLocalVarMap(Map<MethodDeclaration, List<VariableDeclarationFragment>> localVarMap) {
        this.localVarMap = localVarMap;
    }
}
