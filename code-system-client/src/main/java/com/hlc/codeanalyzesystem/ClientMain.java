package com.hlc.codeanalyzesystem;


import com.hlc.codeanalyzesystem.config.security.filter.JwtTokenFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
//@EnableEurekaClient
@MapperScan("com.hlc.codeanalyzesystem.dao")
public class ClientMain {
    public static void main(String[] args) {
        SpringApplication.run(ClientMain.class,args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大   KB,MB
        factory.setMaxFileSize(DataSize.ofMegabytes(100L));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.ofGigabytes(1000L));
        return factory.createMultipartConfig();
    }


}
