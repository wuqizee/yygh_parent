package com.atguigu.yygh.user;

import com.atguigu.yygh.user.prop.WeiXiProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wqz
 * @date 2022/11/10 11:28
 */
@SpringBootApplication
@MapperScan(value = "com.atguigu.yygh.user.mapper")
@ComponentScan(value = "com.atguigu.yygh")
@EnableDiscoveryClient
@EnableFeignClients(value = "com.atguigu.yygh")
@EnableConfigurationProperties(value = WeiXiProperties.class)
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
