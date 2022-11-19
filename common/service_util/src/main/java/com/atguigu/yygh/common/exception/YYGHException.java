package com.atguigu.yygh.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wqz
 * @date 2022/10/29 10:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YYGHException extends RuntimeException {

    private Integer code;
    private String message;

}
