package com.hlc.codeanalyzesystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
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
