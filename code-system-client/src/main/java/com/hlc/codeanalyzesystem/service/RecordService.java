package com.hlc.codeanalyzesystem.service;

import com.hlc.codeanalyzesystem.dao.RecordDao;
import com.hlc.codeanalyzesystem.entities.Record;
import com.hlc.codeanalyzesystem.entities.RecordExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordDao recordDao;

    public int insertRecord(Integer userId,Integer projectId,String fileName,String operation,String resultUrl){
        Record record = new Record();
        record.setUserid(userId);
        record.setProjectid(projectId);
        record.setFilename(fileName);
        record.setOperation(operation);
        record.setOperatedate(new Date());
        record.setOperateresult(resultUrl);
        return recordDao.insertSelective(record);
    }


    public List<Record> selectByUserId(Integer userId, Integer pageNo){
        RecordExample recordExample = new RecordExample();
        RecordExample.Criteria criteria = recordExample.createCriteria();
        criteria.andIdEqualTo(userId);
        recordExample.setLeftLimit((pageNo - 1) * 8);
        recordExample.setLimitSize(8);
        return recordDao.selectByExample(recordExample);
    }
}
