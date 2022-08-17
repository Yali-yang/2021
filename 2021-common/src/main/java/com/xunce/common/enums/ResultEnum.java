package com.xunce.common.enums;

public enum ResultEnum {
    SUCCESS(0, "接口调用成功"),
    VALIDATE_FAILED(202, "参数校验失败"),
    COMMON_FAILED(203, "接口调用失败"),
    FORBIDDEN(204, "权限不足");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
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
