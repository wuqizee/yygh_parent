package com.atguigu.yygh.hosp.test.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author wqz
 * @date 2022/11/4 8:37
 */
@Document("user")
@Data
public class User {

    @Id
    private String id;

    @Field("name")
    private String name;

    private Integer age;

    private Boolean gender;

}
