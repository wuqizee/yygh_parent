package com.atguigu.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wqz
 * @date 2022/10/28 16:12
 */
@SpringBootApplication
@MapperScan(value = "com.atguigu.yygh.hosp.mapper")
@ComponentScan(value = "com.atguigu.yygh")
@EnableDiscoveryClient
@EnableFeignClients(value = "com.atguigu.yygh")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
