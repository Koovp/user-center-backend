package com.pingv.usercenter.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pingv.usercenter.common.BaseResponse;
import com.pingv.usercenter.common.ResponseCode;
import com.pingv.usercenter.common.ResultUtils;
import com.pingv.usercenter.exception.BusinessException;
import com.pingv.usercenter.model.domain.User;
import com.pingv.usercenter.model.request.UserLoginRequest;
import com.pingv.usercenter.model.request.UserRegisterRequest;
import com.pingv.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pingv.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.pingv.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author pingv
 * @date 2023/5/17
 * @apiNote
 */
@RestController
@RequestMapping(("/user"))
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResponseCode.NULL_ERROR);
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ResponseCode.NULL_ERROR);
        }
        return ResultUtils.success(userService.userLogin(userAccount, userPassword, request));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogout(request));
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ResponseCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(username)) {
            queryWrapper.eq("username", username);
        }
        List<User> list = userService.list(queryWrapper);
        List<User> userList = list.stream().map(user -> userService.getSafetyUser(user)).toList();
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
            throw new BusinessException(ResponseCode.NO_AUTH);
        }

        if (id <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/currentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN);
        }
        long id = user.getId();
        // TODO 校验用户是否合法
        return ResultUtils.success(userService.getSafetyUser(userService.getById(id)));
    }

    /**
     * 判断是否为管理员
     *
     * @param request 请求
     * @return true/false
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可以查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
