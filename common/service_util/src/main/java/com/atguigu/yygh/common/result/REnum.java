package com.atguigu.yygh.common.result;

/**
 * @author wqz
 * @date 2022/10/28 20:29
 */
public enum REnum {
    SUCCESS(true, 20000, "成功"),
    ERROR(false, 20001, "失败");

    private Boolean success;
    private Integer code;
    private String message;

    REnum(Boolean success, Integer status, String message) {
        this.success = success;
        this.code = status;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
