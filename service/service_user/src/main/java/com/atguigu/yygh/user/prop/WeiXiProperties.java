package com.atguigu.yygh.user.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wqz
 * @date 2022/11/11 12:51
 */
@Data
@ConfigurationProperties(prefix = "weixi")
public class WeiXiProperties {

    private String appid;

    private String secret;

    private String uri;

}
