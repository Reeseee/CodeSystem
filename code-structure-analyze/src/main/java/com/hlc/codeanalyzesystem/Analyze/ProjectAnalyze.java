package com.hlc.codeanalyzesystem.Analyze;

import com.alibaba.fastjson.JSON;
import com.hlc.codeanalyzesystem.entity.Fileresult;
import com.hlc.codeanalyzesystem.entity.Graph;
import com.hlc.codeanalyzesystem.entity.ProjectresultWithBLOBs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class ProjectAnalyze {

    // 项目注释总行数
    int projectAnnotationCount = 0;
    // 项目总行数
    int projectLineCount = 0;
    // 项目声明总行数
    int projectStatementCount = 0;
    // 项目执行总行数
    int projectExecuteCount = 0;
    // 项目空白总行数
    int projectBlankLine = 0;
    // 项目文件总数
    int projectFileCount = 0;
    // 项目类数
    int projectClassCount = 0;
    // 项目函数数
    int projectFunctionCount = 0;

    // 行数最多的文件
    String longestFile;
    int maxFileLineCount = 0;
    // 行数最多的类
    String longestClass;
    int maxClassLineCount = 0;
    // 行数最多的函数
    String longestFunction;

    int fileIdCount = 0;

    int maxFunctionLineCount = 0;

    Set<String> projectPackage = new HashSet<>();

    String projectDir = "";

    FileAnalyze fileAnalyze = new FileAnalyze();

    List<FileDirVo> projectDirVo = new ArrayList<>();

    Map<String,Integer> fileMap = new HashMap<>();

    Map<Integer,String> idNameMapping = new HashMap<>();


    public List<FileDirVo> showDirectory(String projectId){
        dfsFileDir(projectDir + projectId,0);
        return projectDirVo;
    }

    private void dfsFileDir(String filePath,int depth) {
        File f = new File(filePath);
        for(File file : f.listFiles())
        {
            if(file.isDirectory())
            {
                projectDirVo.add(new FileDirVo(file.getAbsolutePath(),file.getName(),depth));
                dfsFileDir(file.getAbsolutePath(),depth+1);
            }
            else
            {
                projectDirVo.add(new FileDirVo(file.getName(),file.getAbsolutePath(),depth));
            }
        }
    }

    public void projectInitialAnalyze(Integer projectId, ProjectresultWithBLOBs projectresultWithBLOBs){
        dfsFileAnalyze(projectDir + projectId,0);
        projectresultWithBLOBs.setId(projectId);
        projectresultWithBLOBs.setFilecount(projectFileCount);
        projectresultWithBLOBs.setClasscount(projectClassCount);
        projectresultWithBLOBs.setFunctioncount(projectFunctionCount);
        projectresultWithBLOBs.setLinecount(projectLineCount);
        projectresultWithBLOBs.setLongestclass(longestClass);
        projectresultWithBLOBs.setLongestfile(longestFile);
        projectresultWithBLOBs.setProjectdir(JSON.toJSONString(projectDirVo));
        projectresultWithBLOBs.setProjectpackage(JSON.toJSONString(projectPackage));
        Graph graph = projectDependencyAnalyze(projectId);
        projectresultWithBLOBs.setDependency(JSON.toJSONString(graph));
        Graph callGraph = projectCallAnalyze(projectId);
        projectresultWithBLOBs.setFilecallrelation(JSON.toJSONString(callGraph));
    }

    public void dfsFileAnalyze(String fileName,int depth){
        File f = new File(fileName);
        for(File file : f.listFiles())
        {
            if(file.isDirectory())
            {
                projectDirVo.add(new FileDirVo(file.getAbsolutePath(),file.getName(),depth));
                dfsFileAnalyze(file.getAbsolutePath(),depth+1);
            }
            else
            {
                projectDirVo.add(new FileDirVo(file.getName(),file.getAbsolutePath(),depth));
                projectFileCount++;
                try {
                    fileAnalyze.init();
                    fileAnalyze.fileAnalyze(fileName);
                    projectAnnotationCount += fileAnalyze.getAnnotationCount();
                    projectLineCount += fileAnalyze.getLineCount();
                    projectBlankLine += fileAnalyze.getBlankLine();
                    projectExecuteCount += fileAnalyze.getExecuteCount();
                    projectStatementCount += fileAnalyze.getStatementCount();
                    projectClassCount += fileAnalyze.getClassMap().size();
                    projectFunctionCount += fileAnalyze.getFunctionMap().size();
                    fileMap.put(fileAnalyze.getFullyQualifiedName(),fileIdCount);
                    idNameMapping.put(fileIdCount,fileAnalyze.getFullyQualifiedName());
                    fileIdCount++;
                    if(fileAnalyze.getLineCount() > maxFileLineCount)
                    {
                        maxFileLineCount = fileAnalyze.getLineCount();
                        longestFile = fileName;
                    }
                    if(fileAnalyze.getLongestClassLineCount() > maxClassLineCount)
                    {
                        maxClassLineCount = fileAnalyze.getLongestClassLineCount();
                        longestClass = fileAnalyze.getLongestClass();
                    }
                    if(fileAnalyze.getLongestFunctionLineCount() > maxFunctionLineCount)
                    {
                        maxFunctionLineCount = fileAnalyze.getLongestFunctionLineCount();
                        longestFunction = fileAnalyze.getLongestFunction();
                    }
                    projectPackage.add(fileAnalyze.getFullyQualifiedName());
                    projectPackage.add(fileAnalyze.getPackgeName()+".*");
                    projectPackage.addAll(fileAnalyze.getInnerClass());
                } catch (Exception e) {
                    System.out.println("FileAnalyzeException:"+fileName);
                }
            }
        }
    }

    public Graph projectDependencyAnalyze(Integer projectId){
        int [][]dependencyGraph = new int [fileIdCount][fileIdCount];
        dfsFileDependencyAnalyze(projectDir + projectId,dependencyGraph);
        return new Graph(dependencyGraph,idNameMapping,fileMap);
    }

    public void dfsFileDependencyAnalyze(String fileName,int [][]dependencyGraph){
        File f = new File(fileName);
        for(File file : f.listFiles())
        {
            if(file.isDirectory())
            {
                dfsFileDependencyAnalyze(file.getAbsolutePath(),dependencyGraph);
            }
            else
            {
                try {
                    fileAnalyze.init();
                    Set<String> dependencySet = fileAnalyze.importAnalyze(projectPackage,fileName);
                    int from = fileMap.get(fileAnalyze.getFullyQualifiedName());
                    for(String toClass : dependencySet)
                    {
                        int to = fileMap.get(toClass);
                        dependencyGraph[from][to] = 1;
                    }
                } catch (Exception e) {
                    System.out.println("FileDependencyAnalyzeException:"+fileName);
                }
            }
        }
    }

    public Graph projectCallAnalyze(Integer projectId){
        int [][]callGraph = new int [fileIdCount][fileIdCount];
        dfsFileCallAnalyze(projectDir + projectId,callGraph);
        return new Graph(callGraph,idNameMapping,fileMap);
    }

    public void dfsFileCallAnalyze(String fileName,int [][]callGraph){
        File f = new File(fileName);
        for(File file : f.listFiles())
        {
            if(file.isDirectory())
            {
                dfsFileDependencyAnalyze(file.getAbsolutePath(),callGraph);
            }
            else
            {
                try {
                    fileAnalyze.init();
                    fileAnalyze.fileAnalyze(fileName);
                    Set<String> callSet = fileAnalyze.callAnalyze(projectPackage,fileName);
                    int from = fileMap.get(fileAnalyze.getFullyQualifiedName());
                    for(String toClass : callSet)
                    {
                        int to = fileMap.get(toClass);
                        callGraph[from][to] = 1;
                    }
                } catch (Exception e) {
                    System.out.println("FileDependencyAnalyzeException:"+fileName);
                }
            }
        }
    }

}
