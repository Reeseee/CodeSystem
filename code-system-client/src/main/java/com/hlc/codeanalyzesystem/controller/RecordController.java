package com.hlc.codeanalyzesystem.controller;

import com.hlc.codeanalyzesystem.entities.Record;
import com.hlc.codeanalyzesystem.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/record")
@CrossOrigin
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
}
