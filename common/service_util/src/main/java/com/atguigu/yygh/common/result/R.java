package com.atguigu.yygh.common.result;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wqz
 * @date 2022/10/28 20:26
 */
@Getter
public class R {

    private Boolean success;
    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static R ok() {
        R r = new R();
        r.success = REnum.SUCCESS.getSuccess();
        r.code = REnum.SUCCESS.getCode();
        r.message = REnum.SUCCESS.getMessage();
        return r;
    }

    public static R error() {
        R r = new R();
        r.success = REnum.ERROR.getSuccess();
        r.code = REnum.ERROR.getCode();
        r.message = REnum.ERROR.getMessage();
        return r;
    }

    public R success(Boolean success) {
        this.success = success;
        return this;
    }

    public R code(Integer code) {
        this.code = code;
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    public R data(Map<String, Object> date) {
        this.data = date;
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
