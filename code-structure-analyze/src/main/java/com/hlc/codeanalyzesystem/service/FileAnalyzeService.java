package com.hlc.codeanalyzesystem.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead.HalsteadMetrics;
import com.hlc.codeanalyzesystem.JDT.FileJDTResultModel;
import com.hlc.codeanalyzesystem.JDT.JDTAnalyze;
import com.hlc.codeanalyzesystem.JDT.JdtAstUtil;
import com.hlc.codeanalyzesystem.dao.FileresultDao;
import com.hlc.codeanalyzesystem.dao.ProjectresultDao;
import com.hlc.codeanalyzesystem.entity.*;
import com.hlc.codeanalyzesystem.util.TransformUtil;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FileAnalyzeService {

    @Autowired
    private FileresultDao fileresultDao;

    @Autowired
    private ProjectresultDao projectresultDao;

    @Autowired
    private JDTAnalyze jdtAnalyze;

    String resourcePath = "";

    public List<Fileresult> selectFileResultByPidAndFilename(Integer pid,String filename){
        FileresultExample fileresultExample = new FileresultExample();
        FileresultExample.Criteria criteria = fileresultExample.createCriteria();
        criteria.andBelongtoEqualTo(pid);
        criteria.andNameEqualTo(filename);
        return fileresultDao.selectByExampleWithBLOBs(fileresultExample);
    }

    public FileResultVo fileInitialAnalyze(Integer pid,String filename) throws Exception {
        List<Fileresult> fileresultList = selectFileResultByPidAndFilename(pid,filename);
        if(fileresultList == null || fileresultList.size() == 0)
        {
            String absolutePath = filename;
            FileJDTResultModel fileJDTResultModel = jdtAnalyze.fileJDTAnalyze(absolutePath);
            Fileresult fileresult = TransformUtil.transformFileJdtResultToFileResult(fileJDTResultModel);
            fileresult.setBelongto(pid);
            fileresult.setName(filename);
            fileresultDao.insertSelective(fileresult);
            ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(pid);
            Map<String, HalsteadMetrics> fileComp = (Map<String, HalsteadMetrics>) JSON.parse(projectresultWithBLOBs.getComplexfile());
            FileResultVo fileResultVo = TransformUtil.transformFileResultToVo(fileresult);
            fileResultVo.setFileHalstead(JSON.toJSONString(fileComp.get(filename)));
            return fileResultVo;
        }
        else
        {
            ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(pid);
            Map<String, HalsteadMetrics> fileComp = (Map<String, HalsteadMetrics>) JSON.parse(projectresultWithBLOBs.getComplexfile());
            FileResultVo fileResultVo = TransformUtil.transformFileResultToVo(fileresultList.get(0));
            fileResultVo.setFileHalstead(JSON.toJSONString(fileComp.get(filename)));
            return fileResultVo;
        }

    }

    public FileDependencyVo fileDependency(Integer pid,String filename) throws Exception{
        ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(pid);
        Graph graph = JSON.parseObject(projectresultWithBLOBs.getDependency(),Graph.class);
        Map<Integer,String> idNameMap = graph.getIdNameMapping();
        int [][]g = graph.getGraph();
        int pos = 0;
        for (Map.Entry<Integer,String> entry : idNameMap.entrySet())
        {
            if (filename.equals(entry.getValue()))
            {
                pos = entry.getKey();
            }
        }
        int n = g.length;
        FileDependencyVo fileDependencyVo = new FileDependencyVo();
        for(int i=0;i<n;i++)
        {
            if(g[i][pos] == 1)
            {
                fileDependencyVo.getDependency().add(idNameMap.get(i));
            }
        }
        for(int i=0;i<n;i++)
        {
            if(g[pos][i] == 1)
            {
                fileDependencyVo.getBeDependentOn().add(idNameMap.get(i));
            }
        }

        return fileDependencyVo;
    }

    public FileDependencyVo fileCall(Integer pid,String filename) throws Exception {
        ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(pid);
        Graph graph = JSON.parseObject(projectresultWithBLOBs.getFilecallrelation(),Graph.class);
        Map<Integer,String> idNameMap = graph.getIdNameMapping();
        int [][]g = graph.getGraph();
        int pos = 0;
        for (Map.Entry<Integer,String> entry : idNameMap.entrySet())
        {
            if (filename.equals(entry.getValue()))
            {
                pos = entry.getKey();
            }
        }

        int n = g.length;
        FileDependencyVo fileDependencyVo = new FileDependencyVo();
        for(int i=0;i<n;i++)
        {
            if(g[i][pos] == 1)
            {
                fileDependencyVo.getDependency().add(idNameMap.get(i));
            }
        }
        for(int i=0;i<n;i++)
        {
            if(g[pos][i] == 1)
            {
                fileDependencyVo.getBeDependentOn().add(idNameMap.get(i));
            }
        }
        return fileDependencyVo;
    }

    public List<TreeNodeVo> fileAst(Integer pid,String filename) throws Exception {
        String absolutePath = filename;
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(absolutePath);
        List<TreeNodeVo> list = TransformUtil.transformASTNodeToList(comp);
        list.get(0).setChildren(TransformUtil.buildTree(list,0));
        List<TreeNodeVo> ans = new ArrayList<>();
        ans.add(list.get(0));
        return ans;
    }

    public int calculateFileComplexity(Integer pid,String fileName) throws Exception {
        String absolutePath = resourcePath + "/" + pid + "/" + fileName;
        ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(pid);
        String fileCompJson = projectresultWithBLOBs.getComplexfile();
        Map<String,Integer> map = (Map)JSONObject.parse(fileCompJson);
        return map.getOrDefault(absolutePath,-1);
    }


}
