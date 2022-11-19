package com.atguigu.yygh.common.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wqz
 * @date 2022/10/28 18:50
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket adminDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("管理员组")
                .apiInfo(adminInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    public ApiInfo adminInfo() {
        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("")
                .version("1.0")
                .contact(new Contact("", "", ""))
                .build();
    }

    @Bean
    public Docket userDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("用户组")
                .apiInfo(userInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/user/.*")))
                .build();
    }

    public ApiInfo userInfo() {
        return new ApiInfoBuilder()
                .title("用户系统-API文档")
                .description("")
                .version("1.0")
                .contact(new Contact("", "", ""))
                .build();
    }

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .groupName("医院组")
                .apiInfo(apiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("其他服务对接-API文档")
                .description("")
                .version("1.0")
                .contact(new Contact("", "", ""))
                .build();
    }

}
