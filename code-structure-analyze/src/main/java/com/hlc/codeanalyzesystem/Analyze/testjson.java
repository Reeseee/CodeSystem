package com.hlc.codeanalyzesystem.Analyze;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testjson {
    public static void main(String[] args) {
        FileDirVo fileDirVo = new FileDirVo("xxx.xxx","xxx",2);
        FileDirVo fileDirVo1 = new FileDirVo("aaa.xxx.aaa","aaa",3);
        List<FileDirVo> list = new ArrayList<>();
        list.add(fileDirVo);
        list.add(fileDirVo1);
        String jsons = JSON.toJSONString(list);
        List<FileDirVo> l = JSON.parseArray(jsons,FileDirVo.class);
        for(FileDirVo f : l)
        {
            System.out.println(f);
        }
    }
}
