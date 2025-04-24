package com.example.manager.service;

import com.example.manager.domain.dto.auth.ChangePasswordDto;
import com.example.manager.domain.dto.auth.UserLoginDTO;
import com.example.manager.domain.dto.auth.UserRegisterDTO;
import com.example.manager.domain.dto.user.UserUpdateDTO;
import com.example.manager.domain.vo.auth.TokenVO;
import com.example.manager.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author phos
 * @since 2025-04-16
 */
public interface IUserService extends IService<User> {

    /**
     * 注册用户
     *
     * @param dto 注册信息
     */
    void register(UserRegisterDTO dto);

    /**
     * 用户登录
     *
     * @param dto 登录信息
     * @return token信息
     */
    TokenVO login(UserLoginDTO dto);

    /**
     * 刷新token
     *
     * @param refreshToken 刷新token
     * @return 新的token信息
     */
    TokenVO refreshToken(String refreshToken);

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    User getUserById(Integer id);

    /**
     * 更新用户信息
     *
     * @param dto 用户更新信息
     */
    void updateUserInfo(UserUpdateDTO dto);

    void changePassword(@Valid ChangePasswordDto dto);
}
