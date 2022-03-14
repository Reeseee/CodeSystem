package com.hlc.codeanalyzesystem.service;

import com.alibaba.fastjson.JSON;
import com.hlc.codeanalyzesystem.JDT.JDTAnalyze;
import com.hlc.codeanalyzesystem.dao.ProjectresultDao;
import com.hlc.codeanalyzesystem.entity.Graph;
import com.hlc.codeanalyzesystem.entity.ProjectResultVo;
import com.hlc.codeanalyzesystem.entity.ProjectresultWithBLOBs;
import com.hlc.codeanalyzesystem.util.TransformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectAnalyzeService {

    @Autowired
    private ProjectresultDao projectresultDao;

    @Autowired
    private JDTAnalyze jdtAnalyze;


    public ProjectResultVo projectInitialAnalyze(Integer id) throws Exception {
        ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(id);
        ProjectResultVo projectResultVo;
        if(projectresultWithBLOBs != null)
        {
            projectResultVo = TransformUtil.transformProjectResultDoToVo(projectresultWithBLOBs);
        }
        else
        {
            projectresultWithBLOBs = new ProjectresultWithBLOBs();
            projectresultWithBLOBs.setId(id);
            jdtAnalyze.projectInitialAnalyze(id,projectresultWithBLOBs);
            projectresultDao.insertSelective(projectresultWithBLOBs);
            projectResultVo = TransformUtil.transformProjectResultDoToVo(projectresultWithBLOBs);
        }
        return projectResultVo;
    }

    public Graph dependencyGraph(Integer id){
        ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(id);
        Graph graph = JSON.parseObject(projectresultWithBLOBs.getDependency(),Graph.class);
        return graph;
    }


    public Graph callGraph(Integer id){
        ProjectresultWithBLOBs projectresultWithBLOBs = projectresultDao.selectByPrimaryKey(id);
        Graph graph = JSON.parseObject(projectresultWithBLOBs.getFilecallrelation(),Graph.class);
        return graph;
    }
}
