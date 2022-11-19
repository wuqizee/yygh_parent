package com.atguigu.yygh.oss.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author wqz
 * @date 2022/11/12 15:10
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss")
@PropertySource(value = "classpath:oss.properties")
public class OSSProperties {

    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucketName;

}
