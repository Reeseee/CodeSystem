package com.hlc.codeanalyzesystem.controller;

import com.alibaba.fastjson.JSON;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap.Ex;
import com.hlc.codeanalyzesystem.entity.*;
import com.hlc.codeanalyzesystem.service.FileAnalyzeService;
import com.hlc.codeanalyzesystem.service.ProjectAnalyzeService;
import com.hlc.codeanalyzesystem.service.RecordService;
import com.hlc.codeanalyzesystem.util.TransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.List;

@RestController
@RequestMapping("/fileAnalyze")
@Slf4j
@CrossOrigin
public class FileAnalyzeController {

    @Autowired
    private FileAnalyzeService fileAnalyzeService;

    @Autowired
    private RecordService recordService;

    //文件概况
    @RequestMapping("/initial/{pid}")
    public FileResultVo fileInitialAnalyze(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName,@RequestParam("userId") Integer userId){
        try {
            FileResultVo fileresult = fileAnalyzeService.fileInitialAnalyze(pid,fileName);
            recordService.insertRecord(userId,pid,fileName,"fileInitialAnalyze","/fileAnalyze/initial/"+ pid + "?" +"userId = " + userId + "&fileName=" + fileName);
            return fileresult;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.info("fileInitialAnalyze exception  " + fileName);
            return null;
        }
    }

    //文件依赖图
    @RequestMapping("/dependency/{pid}")
    public FileDependencyVo fileDependencyGraph(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName,@RequestParam("userId") Integer userId){
        try {
            FileDependencyVo fileDependencyVo = fileAnalyzeService.fileDependency(pid,fileName);
            recordService.insertRecord(userId,pid,fileName,"fileDependencyGraph","/fileAnalyze/dependency/"+ pid + "?" +"userId = " + userId + "&fileName=" + fileName);
            return fileDependencyVo;
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("fileDependencyAnalyze exception" );
            return null;
        }
    }

    //文件调用图
    @RequestMapping("/call/{pid}")
    public FileDependencyVo fileCallGraph(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName,@RequestParam("userId") Integer userId){
        try {
            FileDependencyVo fileDependencyVo = fileAnalyzeService.fileCall(pid,fileName);
            recordService.insertRecord(userId,pid,fileName,"fileCallGraph","/fileAnalyze/call/"+ pid + "?" +"userId = " + userId + "&fileName=" + fileName);
            return fileDependencyVo;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.info("fileCallAnalyze exception" );
            return null;
        }
    }

    //文件抽象语法树
    @RequestMapping("/ast/{pid}")
    public List<TreeNodeVo> fileAST(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName, @RequestParam("userId") Integer userId){
        try {
            List<TreeNodeVo> tree = fileAnalyzeService.fileAst(pid,fileName);
            recordService.insertRecord(userId,pid,fileName,"fileASTGraph","/fileAnalyze/ast/"+ pid + "?" +"userId = " + userId + "&fileName=" + fileName);
            return tree;
        }
        catch(Exception e) {
            log.info("fileAstAnalyze exception" );
            return null;
        }
    }


//    //文件复杂度
//    @RequestMapping("/complexity")
//    public int fileComplexity(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName,@RequestParam("userId") Integer userId){
//        try {
//            int comp = fileAnalyzeService.calculateFileComplexity(pid,fileName);
//            recordService.insertRecord(userId,pid,fileName,"fileComplexity","/fileAnalyze/"+ pid + "/complexity?" +"userId = " + userId + "&fileName=" + fileName);
//            return comp;
//        }
//        catch (Exception e)
//        {
//            log.info("calculate complexity exception");
//            return -1;
//        }
//    }

    //导出文件依赖关系
    @RequestMapping("/dependency/export/{pid}")
    public int exportFileDependencyGraph(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName, @RequestParam("userId") Integer userId, HttpServletResponse response) {
        try {
            if (fileName != null) {
                FileDependencyVo fileDependencyVo = fileAnalyzeService.fileDependency(pid, fileName);
                List<String> exports = TransformUtil.transformFileDependencyVoToList(fileDependencyVo);
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
                    log.info("dependency export exception");
                    return -1;
                }
            }
            recordService.insertRecord(userId, pid, fileName, "fileDependencyGraph", "/fileAnalyze/dependency/export/" + pid + "?" + "userId = " + userId + "&fileName=" + fileName);
            return 0;
        } catch (Exception e) {
            log.info("fileDependencyAnalyze exception");
            return -1;
        }
    }

    //导出文件调用关系图
    @RequestMapping("/call/export/{pid}")
    public int exportFileCallGraph(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName,@RequestParam("userId") Integer userId,HttpServletResponse response){
        try {
            if (fileName != null) {
                FileDependencyVo fileDependencyVo = fileAnalyzeService.fileCall(pid, fileName);
                List<String> exports = TransformUtil.transformFileDependencyVoToList(fileDependencyVo);
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
            recordService.insertRecord(userId, pid, fileName, "fileCallGraph", "/fileAnalyze/call/export/" + pid + "?" + "userId = " + userId + "&fileName=" + fileName);
            return 0;
        } catch (Exception e) {
            log.info("fileCallAnalyze exception");
            return -1;
        }
    }

    //导出抽象语法树
    @RequestMapping("/ast/export/{pid}")
    public int exportFileAST(@PathVariable("pid") Integer pid, @RequestParam("fileName") String fileName,@RequestParam("userId") Integer userId,HttpServletResponse response){
        try {
            if (fileName != null) {
                String exports = JSON.toJSONString(fileAnalyzeService.fileAst(pid, fileName));
                String downloadFileName = System.currentTimeMillis()+".txt";
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + downloadFileName);// 设置文件名
                try {
                    OutputStream os = response.getOutputStream();
                    os.write(exports.getBytes());
                } catch (Exception e) {
                    log.info("ast export exception");
                    return -1;
                }
            }
            recordService.insertRecord(userId, pid, fileName, "fileAst", "/fileAnalyze/ast/export/" + pid + "?" + "userId = " + userId + "&fileName=" + fileName);
            return 0;
        }
        catch(Exception e) {
            log.info("fileAstExport exception" );
            return -1;
        }
    }


}
