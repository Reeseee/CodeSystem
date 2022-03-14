package com.hlc.codeanalyzesystem;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@SpringBootApplication
@EnableEurekaClient
public class CodeStructureAnalyzeMain {
    public static void main(String[] args) {
        SpringApplication.run(CodeStructureAnalyzeMain.class,args);
    }
}
