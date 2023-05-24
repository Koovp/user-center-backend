package com.pingv.usercenter.exception;

import com.pingv.usercenter.common.ResponseCode;

import java.io.Serial;

/**
 * 业务异常类
 *
 * @author pingv
 * @date 2023/5/24
 * @apiNote
 */
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -279448431260194195L;
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ResponseCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ResponseCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

