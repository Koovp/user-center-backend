package com.pingv.usercenter.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pingv.usercenter.common.ResponseCode;
import com.pingv.usercenter.exception.BusinessException;
import com.pingv.usercenter.model.domain.User;
import com.pingv.usercenter.service.UserService;
import com.pingv.usercenter.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.pingv.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author pingv
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-05-05 22:26:37
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "账号至少是4到20位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8 || userPassword.length() > 20 || checkPassword.length() > 20) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "密码至少是8到20位");
        }

        // 账户不能包含特殊字符,只允许字母和数字 下划线
        String pattern = "^[a-zA-Z0-9_]+$";
        if (!Pattern.matches(pattern, userAccount)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "账户只能包含字母数字下划线");
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "两次密码不一致");
        }

        // 账户不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "账户已存在");
        }

        // 2. 加密
        String encryptPwd = SecureUtil.md5(userPassword);

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPwd);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "注册失败");
        }
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1. 校验
        if (StrUtil.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "参数为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "账号至少是4到20位");
        }
        if (userPassword.length() < 8 || userPassword.length() > 20) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "密码至少是8到20位");
        }

        // 账户不能包含特殊字符,只允许字母和数字 下划线
        String pattern = "^[a-zA-Z0-9_]+$";
        if (!Pattern.matches(pattern, userAccount)) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "账户只能包含字母数字下划线");
        }

        // 2. 加密
        String encryptPwd = SecureUtil.md5(userPassword);

        // 账户不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        userQueryWrapper.eq("user_password", encryptPwd);
        User user = userMapper.selectOne(userQueryWrapper);

        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ResponseCode.NOT_LOGIN, "账户或密码错误");
        }

        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);

        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;

    }

    /**
     * 用户脱敏
     *
     * @param originUser 未脱敏的用户信息
     * @return 已脱敏的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;

    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 0;
    }


}




