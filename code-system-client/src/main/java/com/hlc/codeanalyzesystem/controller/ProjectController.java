package com.hlc.codeanalyzesystem.controller;

import com.hlc.codeanalyzesystem.entities.Project;
import com.hlc.codeanalyzesystem.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/myProject")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public List<Project> projectList(@RequestParam("userId") Integer userId,@RequestParam(value = "pageNo",required = false) Integer pageNo) {
        if(userId == null) {
            log.info("userId could not be null");
            return null;
        }
        if(pageNo == null){
            pageNo = 1;
        }
        List<Project> projectList = projectService.queryProjectByUserIdAndPageNo(userId,pageNo);
        return projectList;
    }

    @RequestMapping(value="/listCount",method= RequestMethod.GET)
    public Long projectListCount(@RequestParam("userId") Integer userId) {
        if(userId == null) {
            log.info("userId could not be null");
            return null;
        }
        Long projectCount = projectService.queryProjectCountByUserId(userId);
        return projectCount;
    }


    @RequestMapping(value="/upload",method= RequestMethod.POST)
    public int upload(@RequestParam("dir") MultipartFile[] dir, @RequestParam("userDefineName") String projectName, @RequestParam("userId") Integer userId) {
        try {
            projectService.saveProject(dir,projectName,userId);
        }
        catch (Exception e) {
            log.info("project upload Exception");
            return -1;
        }
        return 0;
    }
}
