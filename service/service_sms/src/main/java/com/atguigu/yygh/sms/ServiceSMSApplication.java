package com.atguigu.yygh.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wqz
 * @date 2022/11/10 16:38
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(value = "com.atguigu.yygh")
@EnableDiscoveryClient
public class ServiceSMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSMSApplication.class, args);
    }
}
