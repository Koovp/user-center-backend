package com.pingv.usercenter.service;
import java.util.Date;
import java.util.regex.Pattern;


import cn.hutool.core.util.ReUtil;
import com.pingv.usercenter.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description 用户服务测试
 * @author pingv
 * @date 2023/5/5
 * @apiNote
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){

        User user = new User();

        user.setUsername("Pingv");
        user.setUserAccount("pingv");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("13600000000");
        user.setEmail("xxx@qq.com");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);

    }

    @Test
    public void testValidAccount(){
        String pattern = "^[a-zA-Z0-9_]+$";

        // 账户不能包含特殊字符
        String userAccount = "abc124asldfals_";
        boolean match = Pattern.matches(pattern, userAccount);
        assertTrue(match);

    }

    @Test
    void userRegister() {

        // 不能为空
        String userAccount = "";
        String userPassword = "";
        String checkPassword = "";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账户不能小于4位
        userAccount = "gu";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 密码不能小于8位
        userAccount = "guwp";
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 密码和确认密码必须一样
        userAccount = "guwp";
        userPassword = "12345678";
        checkPassword = "12345679";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号只能数字字母下划线
        userAccount = "gu wp";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号不能存在相同的账号
        userAccount = "pingv";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "guwp";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(3, result);



    }

    @Test
    void userLogin() {



    }

    @Test
    void getSafetyUser() {
    }
}