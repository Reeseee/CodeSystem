package com.hlc.codeanalyzesystem.service;


import com.hlc.codeanalyzesystem.dao.ProjectDao;
import com.hlc.codeanalyzesystem.entities.Project;
import com.hlc.codeanalyzesystem.entities.ProjectExample;
import com.hlc.codeanalyzesystem.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {

    public static final String PROJECT_DIR = "D://resource//";

    @Autowired
    ProjectDao projectDao;

    public List<Project> queryProjectByUserId(Integer userId){
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria criteria = projectExample.createCriteria();
        criteria.andUseridEqualTo(userId);
        List<Project> projectList = projectDao.selectByExample(projectExample);
        return projectList;
    }

    public List<Project> queryProjectByUserIdAndPageNo(Integer userId,Integer pageNo){
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria criteria = projectExample.createCriteria();
        criteria.andUseridEqualTo(userId);
        projectExample.setLeftLimit((pageNo - 1) * 8);
        projectExample.setLimitSize(8);
        List<Project> projectList = projectDao.selectByExample(projectExample);
        return projectList;
    }

    public long queryProjectCountByUserId(Integer userId){
        ProjectExample projectExample = new ProjectExample();
        ProjectExample.Criteria criteria = projectExample.createCriteria();
        criteria.andUseridEqualTo(userId);
        long cnt = projectDao.countByExample(projectExample);
        return cnt % 8 == 0 ? cnt/8 : cnt/8 + 1;
    }


    public int insertProject(String projectName,Integer userId){
        Project project = new Project();
        project.setProjectname(projectName);
        project.setUploaddate(new Date());
        project.setUserid(userId);
        projectDao.insertSelective(project);
        Integer id = project.getId();
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProject(MultipartFile[] dir,String projectName,Integer userId) throws Exception {
        File file;
        String fileName="";
        String filePath="";
        Integer pid = insertProject(projectName,userId);
        if(pid == null){
            throw new Exception("insert exception");
        }

        for (MultipartFile f : dir) {
            //todo 这里可能存在问题，不同浏览器这个获取结果不一样
            fileName=f.getOriginalFilename();
            
            filePath=PROJECT_DIR + pid + "//" + fileName.substring(0,fileName.lastIndexOf("/"));
            if(!FileUtil.isDir(filePath)){
                FileUtil.makeDirs(filePath);
            }
            file = new File(PROJECT_DIR + pid + "//" + fileName);
            file.createNewFile();
            f.transferTo(file);
        }
        return ;
    }
}
