package com.pingv.usercenter.common;

/**
 *
 * 返回工具类
 *
 * @author pingv
 * @date 2023/5/24
 * @apiNote
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 返回数据
     * @param <T> 返回数据类型
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param responseCode 状态码
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(ResponseCode responseCode) {
        return new BaseResponse(responseCode);
    }

    /**
     * 失败
     *
     * @param code 状态码
     * @param message 状态码信息
     * @param description 状态码描述
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse(code, null, message, description);
    }

    /**
     * 失败
     *
     * @param responseCode 状态码
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(ResponseCode responseCode, String message, String description) {
        return new BaseResponse(responseCode.getCode(), null, message, description);
    }

    /**
     * 失败
     *
     * @param responseCode 状态码
     * @return BaseResponse
     */
    public static <T> BaseResponse<T> error(ResponseCode responseCode, String description) {
        return new BaseResponse(responseCode.getCode(), responseCode.getMessage(), description);
    }
}

