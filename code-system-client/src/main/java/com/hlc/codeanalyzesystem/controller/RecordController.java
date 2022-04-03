package com.hlc.codeanalyzesystem.controller;

import com.hlc.codeanalyzesystem.entities.Record;
import com.hlc.codeanalyzesystem.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @RequestMapping("/list")
    public List<Record> MyRecordList(@RequestParam("userId") Integer userId,@RequestParam(value = "pageNo",required = false) Integer pageNo){
        if(userId == null){
            return null;
        }
       if(pageNo == null)
       {
           pageNo = 1;
       }
       return recordService.selectByUserId(userId,pageNo);
    }

    @RequestMapping("/pageCount")
    public Long MyRecordPageCount(@RequestParam("userId") Integer userId){
        log.info("record count");
        if(userId == null){
            return null;
        }
        return recordService.getPageCount(userId);
    }
}
