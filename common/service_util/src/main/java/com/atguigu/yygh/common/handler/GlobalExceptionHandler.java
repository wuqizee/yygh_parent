package com.atguigu.yygh.common.handler;

import com.atguigu.yygh.common.exception.YYGHException;
import com.atguigu.yygh.common.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author wqz
 * @date 2022/10/29 8:48
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R handlerException(Exception ex) {
        ex.printStackTrace();
        log.error("Exception:" + ex.getMessage());
        return R.error().message("全局处理异常");
    }

    @ExceptionHandler(ArithmeticException.class)
    public R handlerArithmeticException(ArithmeticException ex) {
        //ex.printStackTrace();
        log.error("ArithmeticException:" + ex.getMessage());
        return R.error().message(ex.getMessage());
    }

    @ExceptionHandler(YYGHException.class)
    public R handlerArithmeticException(YYGHException ex) {
        //ex.printStackTrace();
        log.error("YYGHException:" + ex.getMessage());
        return R.error().message(ex.getMessage());
    }

}
