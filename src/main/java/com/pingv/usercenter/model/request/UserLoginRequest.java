package com.pingv.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 * @author pingv
 * @date 2023/5/17
 * @apiNote
 */
@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4682138723267993928L;

    private String userAccount;

    private String userPassword;
}
