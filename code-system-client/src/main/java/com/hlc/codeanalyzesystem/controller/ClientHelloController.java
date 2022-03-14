package com.hlc.codeanalyzesystem.controller;

import com.hlc.codeanalyzesystem.service.ProjectService;
import com.hlc.codeanalyzesystem.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@RestController
@Slf4j
@CrossOrigin
public class ClientHelloController {

    //public static final String PAYMENT_URL = "http://localhost:8001";

    public static final String PAYMENT_URL = "http://CODE-STRUCTURE-ANALYZE";

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/hello")
    public String testHello(){
        System.out.println("come");
        return restTemplate.getForObject(PAYMENT_URL+"/hello",String.class);
    }

    @RequestMapping("/hello2")
    public String testHello2(){
        return "hh";
    }


}
