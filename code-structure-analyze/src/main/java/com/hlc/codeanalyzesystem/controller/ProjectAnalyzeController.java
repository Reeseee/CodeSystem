package com.hlc.codeanalyzesystem.controller;

import com.alibaba.fastjson.JSON;
import com.hlc.codeanalyzesystem.entity.FileDependencyVo;
import com.hlc.codeanalyzesystem.entity.G6.G6Graph;
import com.hlc.codeanalyzesystem.entity.Graph;
import com.hlc.codeanalyzesystem.entity.ProjectResultVo;
import com.hlc.codeanalyzesystem.entity.ProjectresultWithBLOBs;
import com.hlc.codeanalyzesystem.service.ProjectAnalyzeService;
import com.hlc.codeanalyzesystem.service.RecordService;
import com.hlc.codeanalyzesystem.util.TransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/projectAnalyze")
@Slf4j
@CrossOrigin
public class ProjectAnalyzeController {

    @Autowired
    private ProjectAnalyzeService projectAnalyzeService;

    @Autowired
    private RecordService recordService;

    //项目概况
    @RequestMapping("/initial/{id}")
    public ProjectResultVo projectInitialAnalyze(@PathVariable("id") Integer id, @RequestParam("userId") Integer userId){
        try {
            ProjectResultVo projectResultVo = projectAnalyzeService.projectInitialAnalyze(id);
            recordService.insertRecord(userId,id,"","ProjectAnalyze","/projectAnalyze/initial/"+ id + "?" +"userId = " + userId);
            System.out.println(JSON.toJSONString(projectResultVo));
            return projectResultVo;
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("project initial exception" + id + " " + userId);
            return null;
        }
    }

    //项目依赖图
    @RequestMapping("/dependency/{id}")
    public G6Graph projectDependencyGraph(@PathVariable("id") Integer id, @RequestParam("userId") Integer userId){
        Graph graph = projectAnalyzeService.dependencyGraph(id);
        G6Graph g6Graph = TransformUtil.transformToG6(graph);
        recordService.insertRecord(userId,id,"","/dependency","/projectAnalyze/dependency/"+ id + "?" +"userId = " + userId);
        return g6Graph;
    }

    //项目调用图
    @RequestMapping("/call/{id}")
    public G6Graph projectCallGraph(@PathVariable("id") Integer id,@RequestParam("userId") Integer userId){
        Graph graph = projectAnalyzeService.callGraph(id);
        G6Graph g6Graph = TransformUtil.transformToG6(graph);
        recordService.insertRecord(userId,id,"","/call","/projectAnalyze/call/"+ id + "?" +"userId = " + userId);
        return g6Graph;
    }

    //导出项目依赖图
    @RequestMapping("/dependency/export/{id}")
    public int projectDependencyGraphExport(@PathVariable("id") Integer id, @RequestParam("userId") Integer userId, HttpServletResponse response){
        try {
            if (id != null) {
                Graph graph = projectAnalyzeService.dependencyGraph(id);
                List<String> exports = TransformUtil.transformGraphToList(graph);
                String downloadFileName = System.currentTimeMillis()+".txt";
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + downloadFileName);// 设置文件名
                try {
                    OutputStream os = response.getOutputStream();
                    for(String str : exports)
                    {
                        os.write(str.getBytes());
                    }
                } catch (Exception e) {
                    log.info("call export exception");
                    return -1;
                }
            }
            //recordService.insertRecord(userId,id,"","ProjectDependencyExport","/projectAnalyze/dependency/export/"+ id + "?" +"userId = " + userId);
            return 0;
        } catch (Exception e) {
            log.info("projectDependencyExport exception");
            return -1;
        }
    }

    //导出项目调用图
    @RequestMapping("/call/export/{id}")
    public int projectCallGraphExport(@PathVariable("id") Integer id,@RequestParam("userId") Integer userId, HttpServletResponse response){
        try {
            if (id != null) {
                Graph graph = projectAnalyzeService.callGraph(id);
                List<String> exports = TransformUtil.transformGraphToList(graph);
                String downloadFileName = System.currentTimeMillis()+".txt";
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + downloadFileName);// 设置文件名
                try {
                    OutputStream os = response.getOutputStream();
                    for(String str : exports)
                    {
                        os.write(str.getBytes());
                    }
                } catch (Exception e) {
                    log.info("call export exception");
                    return -1;
                }
            }
            //recordService.insertRecord(userId,id,"","ProjectCallExport","/projectAnalyze/call/export/"+ id + "?" +"userId = " + userId);
            return 0;
        } catch (Exception e) {
            log.info("projectCallExport exception");
            return -1;
        }
    }

}
