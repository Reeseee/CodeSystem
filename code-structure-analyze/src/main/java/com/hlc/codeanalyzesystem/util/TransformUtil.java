package com.hlc.codeanalyzesystem.util;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead.HalsteadMetrics;
import com.hlc.codeanalyzesystem.JDT.FileJDTResultModel;
import com.hlc.codeanalyzesystem.JDT.JdtAstUtil;
import com.hlc.codeanalyzesystem.entity.*;
import com.hlc.codeanalyzesystem.entity.G6.G6Edge;
import com.hlc.codeanalyzesystem.entity.G6.G6Graph;
import com.hlc.codeanalyzesystem.entity.G6.G6Node;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import java.util.stream.Collectors;

public class TransformUtil {

    static int curId = 1;

    public static Set<String> getClassSetFromString(String s) {
        s = s.substring(1, s.length() - 1);
        String[] classes = s.split("\\.");
        Set<String> classSet = new HashSet<>();
        for (String className : classes) {
            classSet.add(className);
        }
        return classSet;
    }

    public static List<String> transformFileDependencyVoToList(FileDependencyVo fileDependencyVo) {
        List<String> res = new ArrayList<>();
        String dependency = "depends on ";
        for (String s : fileDependencyVo.getDependency()) {
            dependency += s + " ";
        }
        String beDependentOn = "be dependent on ";
        for (String s : fileDependencyVo.getBeDependentOn()) {
            beDependentOn += s + " ";
        }
        res.add(dependency);
        res.add(beDependentOn);
        return res;
    }

    public static Graph transformAstListToGraph(List<String> ast) {
        //todo 将ast转图 不一定需要

        return new Graph();
    }

    public static List<String> transformGraphToList(Graph graph) {
        List<String> graphList = new ArrayList<>();
        Map<Integer, String> idNameMap = graph.getIdNameMapping();
        int[][] g = graph.getGraph();
        int n = g.length;
        //处理矩阵上侧标注
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        for (int i = 0; i < n; i++) {
            sb.append(i + "\t");
        }
        sb.append("\n");
        graphList.add(sb.toString());

        //处理矩阵
        for (int i = 0; i < n; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i + "\t");
            for (int j = 0; j < n; j++) {
                stringBuilder.append(g[i][j] + "\t");
            }
            stringBuilder.append("\n");
            graphList.add(stringBuilder.toString());
        }

        //处理映射
        for (int i = 0; i < n; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(i + "\t" + idNameMap.get(i) + "\n");
            graphList.add(stringBuilder.toString());
        }

        return graphList;
    }

    public static Fileresult transformFileJdtResultToFileResult(FileJDTResultModel fileJDTResultModel) {
        Fileresult fileresult = new Fileresult();
        fileresult.setLinecount(fileJDTResultModel.getLineCount());
        fileresult.setClasslist(JSON.toJSONString(transformTypeDeclarationToStr(fileJDTResultModel.getClasses())));
        fileresult.setClasscount(fileJDTResultModel.getClassCount());
        fileresult.setFunctionlist(JSON.toJSONString(transformMethodDeclarationToStr(fileJDTResultModel.getFunctions())));
        fileresult.setFunctioncount(fileJDTResultModel.getFunctionCount());
        fileresult.setVariablelist(JSON.toJSONString(transformVarDeclarationToStr(fileJDTResultModel.getVars())));
        fileresult.setVariablecount(fileJDTResultModel.getVariableCount());
        fileresult.setLongestfunction(fileJDTResultModel.getLongestFunction());
        fileresult.setLongestclass(fileJDTResultModel.getLongestClass());
        List<FunctionVarList> functionVarLists = transformFunctionVarMapToList(fileJDTResultModel.getLocalVarMap(),fileJDTResultModel.getMethodComplexityMap());
        fileresult.setFunctionvariable(JSONUtil.toJsonStr(functionVarLists));
        return fileresult;
    }

    public static List<String> transformTypeDeclarationToStr(List<TypeDeclaration> classes){
        List<String> classList = new ArrayList<>();
        for (TypeDeclaration typeDeclaration : classes)
        {
            classList.add(typeDeclaration.getName().toString());
        }
        return classList;
    }

    public static List<String> transformMethodDeclarationToStr(List<MethodDeclaration> methodDeclarationList){
        List<String> methodList = new ArrayList<>();
        for (MethodDeclaration methodDeclaration : methodDeclarationList)
        {
            methodList.add(methodDeclaration.getName().toString());
        }
        return methodList;
    }

    public static List<String> transformVarDeclarationToStr(List<VariableDeclarationFragment> variableDeclarationList){
        List<String> varList = new ArrayList<>();
        for (VariableDeclarationFragment variableDeclaration : variableDeclarationList)
        {
            varList.add(variableDeclaration.getName().toString());
        }
        return varList;
    }


    public static List<FunctionVarList> transformFunctionVarMapToList(Map<MethodDeclaration,List<VariableDeclarationFragment>> localVarMap,Map<MethodDeclaration,HalsteadMetrics> functionComplexityMap){
        List<FunctionVarList> functionVarLists = new ArrayList<>();
        int countId = 0;
        for (Map.Entry<MethodDeclaration,List<VariableDeclarationFragment>> entry : localVarMap.entrySet())
        {
            MethodDeclaration key = entry.getKey();
            List<VariableDeclarationFragment> variableDeclarationFragments = entry.getValue();
            List<String> varInfoList = new ArrayList<>();
            for (VariableDeclarationFragment v : variableDeclarationFragments)
            {
                varInfoList.add(JdtAstUtil.extractType(v) + " " + v.getName());
            }
            HalsteadMetrics halsteadMetrics = functionComplexityMap.get(key);
            System.out.println(halsteadMetrics);
            functionVarLists.add(new FunctionVarList(countId++,key.getName().toString(),varInfoList,halsteadMetrics));
        }
        return functionVarLists;
    }

    public static List<TreeNodeVo> transformASTNodeToList(ASTNode node) {
        List<TreeNodeVo> list = new ArrayList<>();
        list.add(new TreeNodeVo(0,-1,"root"));
        curId = 1;
        print(node, list, 0);
        return list;
    }

    private static void print(ASTNode node, List<TreeNodeVo> res, int parentId) {
        List properties = node.structuralPropertiesForType();
        for (Iterator iterator = properties.iterator(); iterator.hasNext(); ) {
            Object descriptor = iterator.next();
            if (descriptor instanceof SimplePropertyDescriptor) {
                SimplePropertyDescriptor simple = (SimplePropertyDescriptor) descriptor;
                Object value = node.getStructuralProperty(simple);
                //res.add(new TreeNodeVo(curId, parentId, value.toString()));
                res.add(new TreeNodeVo(curId, parentId, "Simple  " + simple.getId() + " : " + value.toString()));
                curId++;
                //res.add(simple.getId() + " (" + simple.toString() + "-" + value.toString() + ")");
            } else if (descriptor instanceof ChildPropertyDescriptor) {
                ChildPropertyDescriptor child = (ChildPropertyDescriptor) descriptor;
                ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
                if (childNode != null) {
                    res.add(new TreeNodeVo(curId, parentId, "child " + child.getNodeClass().getSimpleName()+ " " + child.getId()  + " " + childNode.toString()));
                    int tempId = curId;
                    curId++;
                    //res.add("Child (" + child.getId() + child.toString() + ") {");
                    print(childNode, res, tempId);
                    //res.add("}");
                }
            } else {
                ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) descriptor;
                res.add(new TreeNodeVo(curId, parentId,"list " + list.getId()));
                int tempId = curId;
                curId++;
                //res.add("List (" + list.getId() + "){");
                print((List) node.getStructuralProperty(list), res, tempId);
                //res.add("}");
            }
        }
    }

    private static void print(List nodes, List<TreeNodeVo> res, Integer pid) {
        for (Iterator iterator = nodes.iterator(); iterator.hasNext(); ) {
            print((ASTNode) iterator.next(), res, pid);
        }
    }

    public static List<TreeNodeVo> buildTree(List<TreeNodeVo> deptList, int pid){
        List<TreeNodeVo> treeList = new ArrayList<TreeNodeVo>();
        for (TreeNodeVo treeNodeVo : deptList) {
            if (treeNodeVo.getPid() == pid) {
                treeNodeVo.setChildren(buildTree(deptList, treeNodeVo.getId()));
                treeList.add(treeNodeVo);
            }
        }
        return treeList;
    }

    public static List<FileDirNodeVo> buildDirTree(List<FileDirNodeVo> list, int pid){
        List<FileDirNodeVo> treeList = new ArrayList<>();
        for (FileDirNodeVo treeNodeVo : list) {
            if (treeNodeVo.getPid() == pid) {
                treeNodeVo.setChildren(buildDirTree(list, treeNodeVo.getId()));
                treeList.add(treeNodeVo);
            }
        }
        return treeList;
    }

    public static ProjectResultVo transformProjectResultDoToVo(ProjectresultWithBLOBs projectresultWithBLOBs){
        ProjectResultVo projectResultVo = new ProjectResultVo();
        projectResultVo.setId(projectresultWithBLOBs.getId());
        projectResultVo.setLinecount(projectresultWithBLOBs.getLinecount());
        projectResultVo.setFilecount(projectresultWithBLOBs.getFilecount());
        projectResultVo.setClasscount(projectresultWithBLOBs.getClasscount());
        projectResultVo.setFunctioncount(projectresultWithBLOBs.getFunctioncount());
        projectResultVo.setLongestfile(projectresultWithBLOBs.getLongestfile());
        projectResultVo.setLongestclass(projectresultWithBLOBs.getLongestclass());
        projectResultVo.setLongestfunction(projectresultWithBLOBs.getLongestfunction());
        List<FileDirNodeVo> projectDirVo = JSON.parseObject(projectresultWithBLOBs.getProjectdir(),new TypeReference<List<FileDirNodeVo>>(){});
        TransformUtil.buildDirTree(projectDirVo,0);
        projectResultVo.getProjectdir().add(projectDirVo.get(0));
        String fileCompJson = projectresultWithBLOBs.getComplexfile();
        Map<String,HalsteadMetrics> fileComp = (Map<String, HalsteadMetrics>) JSON.parseObject(fileCompJson,new TypeReference<Map<String,HalsteadMetrics>>(){});
        double maxComplex = -1.0;
        String complexFilePath = "";
        for (Map.Entry<String,HalsteadMetrics> entry : fileComp.entrySet())
        {
            if(entry.getValue().getDifficulty() > maxComplex)
            {
                maxComplex = entry.getValue().getDifficulty();
                complexFilePath = entry.getKey();
            }
        }
        projectResultVo.setMaxComplexity(maxComplex);
        projectResultVo.setMostComplexFile(complexFilePath);
        projectResultVo.setProjectComplexity(projectresultWithBLOBs.getProjectcomplexity());
        return projectResultVo;
    }


    public static FileResultVo transformFileResultToVo(Fileresult fileresult){
        FileResultVo fileResultVo = new FileResultVo();
        fileResultVo.setId(fileresult.getId());
        fileResultVo.setBelongto(fileresult.getBelongto());
        fileResultVo.setLinecount(fileresult.getLinecount());
        fileResultVo.setClasscount(fileresult.getClasscount());
        fileResultVo.setFunctioncount(fileresult.getFunctioncount());
        fileResultVo.setVariablecount(fileresult.getVariablecount());
        fileResultVo.setClasslist(fileresult.getClasslist());
        fileResultVo.setFunctionlist(fileresult.getFunctionlist());
        fileResultVo.setVariablelist(fileresult.getVariablelist());
        fileResultVo.setLongestclass(fileresult.getLongestclass());
        fileResultVo.setLongestfunction(fileresult.getLongestfunction());
        fileResultVo.setFunctionvariable(fileresult.getFunctionvariable());
        return fileResultVo;
    }

    public static G6Graph transformToG6(Graph graph){
        List<G6Node> nodes = new ArrayList<>();
        List<G6Edge> edges = new ArrayList<>();
        Map<Integer,String> idNameMap = graph.getIdNameMapping();
        for(Map.Entry<Integer,String> entry : idNameMap.entrySet())
        {
            nodes.add(new G6Node(entry.getKey().toString(), entry.getValue()));
        }
        int [][]g = graph.getGraph();
        for(int i=0;i<g.length;i++)
        {
            for(int j=0;j<g[0].length;j++)
            {
                if(g[i][j] == 1)
                {
                    edges.add(new G6Edge(String.valueOf(i),String.valueOf(j)));
                }
            }
        }
        return new G6Graph(nodes,edges);
    }


    public static void main(String[] args) {
        String jstr = "{\"graph\":[[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]],\"idNameMapping\":{0:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\alibaba\\\\ali1.java\",1:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\alibaba\\\\ali2.java\",2:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\bytedance.java\",3:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\concurrentPractice\\\\ProducerConsumer.java\",4:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\concurrentPractice\\\\ReltReetLock.java\",5:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\concurrentPractice\\\\RelyBlockingQueue.java\",6:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\hh.java\",7:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\honor\\\\h1.java\",8:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\honor\\\\h2.java\",9:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\honor\\\\h3.java\",10:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\AutumnInterview1.java\",11:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ1.java\",12:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ10.java\",13:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ11.java\",14:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ2.java\",15:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ3.java\",16:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ4.java\",17:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ5.java\",18:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ6.java\",19:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ7.java\",20:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ8.java\",21:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ9.java\",22:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\Interview2.java\",23:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\solution1.java\",24:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\solutionOne.java\",25:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\solutionTwo.java\",26:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\InputTemplate\\\\addInputPractice.java\",27:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\InputTemplate\\\\strInputPrac.java\",28:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\InputTemplate\\\\twoDArrayInputTransFrom.java\",29:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\jingdong\\\\b2.java\",30:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\leetcode\\\\Dec11.java\",31:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\marsLanguage.java\",32:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituan1.java\",33:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituan2.java\",34:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituan5.java\",35:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituanFour.java\",36:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n1.java\",37:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n2.java\",38:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n3.java\",39:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n4.java\",40:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\sangfor\\\\SangforQuestion1.java\",41:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\n1.java\",42:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\n2.java\",43:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\q1.java\",44:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\q3.java\",45:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n2.java\",46:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n3.java\",47:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n4.java\",48:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n5.java\",49:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\repeatNum.java\",50:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\solution1.java\",51:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\water.java\",52:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\wangyi\\\\n2.java\",53:\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\wangyi\\\\q1.java\"},\"nameIdMapping\":{\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\concurrentPractice\\\\ReltReetLock.java\":4,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\honor\\\\h2.java\":8,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\alibaba\\\\ali1.java\":0,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ5.java\":17,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\q1.java\":43,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\AutumnInterview1.java\":10,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\solutionOne.java\":24,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ6.java\":18,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\repeatNum.java\":49,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n5.java\":48,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\Interview2.java\":22,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\solution1.java\":23,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\concurrentPractice\\\\RelyBlockingQueue.java\":5,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ11.java\":13,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n1.java\":36,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ7.java\":19,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\n1.java\":41,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ4.java\":16,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\InputTemplate\\\\strInputPrac.java\":27,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\concurrentPractice\\\\ProducerConsumer.java\":3,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\marsLanguage.java\":31,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\water.java\":51,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\honor\\\\h3.java\":9,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\InputTemplate\\\\twoDArrayInputTransFrom.java\":28,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n3.java\":46,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituan5.java\":34,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituan2.java\":33,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\alibaba\\\\ali2.java\":1,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ1.java\":11,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\q3.java\":44,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituanFour.java\":35,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\meituan1.java\":32,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n3.java\":38,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ3.java\":15,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ8.java\":20,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\solutionTwo.java\":25,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\shoppee\\\\n2.java\":42,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\sangfor\\\\SangforQuestion1.java\":40,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\solution1.java\":50,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ2.java\":14,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\InputTemplate\\\\addInputPractice.java\":26,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\wangyi\\\\q1.java\":53,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n4.java\":39,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n2.java\":45,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ9.java\":21,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\wangyi\\\\n2.java\":52,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\hh.java\":6,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\huawei\\\\huaweiHJ10.java\":12,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\meiTuan\\\\n2.java\":37,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\tencent\\\\n4.java\":47,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\jingdong\\\\b2.java\":29,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\honor\\\\h1.java\":7,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\leetcode\\\\Dec11.java\":30,\"D:\\\\resource\\\\1\\\\com\\\\hlc\\\\bytedance.java\":2}}";
        Graph graph = JSONUtil.toBean(jstr,Graph.class);
        List<String> list = transformGraphToList(graph);
        for (String s : list)
            System.out.print(s);
    }

}
