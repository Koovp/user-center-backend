package com.pingv.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求踢
 * @author pingv
 * @date 2023/5/17
 * @apiNote
 */
@Data
public class UserRegisterRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 7385389214899267819L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
