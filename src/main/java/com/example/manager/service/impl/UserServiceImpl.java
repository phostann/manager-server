package com.example.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.manager.converter.UserConverter;
import com.example.manager.domain.dto.auth.ChangePasswordDto;
import com.example.manager.domain.dto.auth.UserLoginDTO;
import com.example.manager.domain.dto.auth.UserRegisterDTO;
import com.example.manager.domain.dto.user.UserUpdateDTO;
import com.example.manager.domain.vo.auth.TokenVO;
import com.example.manager.entity.User;
import com.example.manager.exception.BusinessException;
import com.example.manager.mapper.UserMapper;
import com.example.manager.properties.AuthConfigProperties;
import com.example.manager.response.ErrorCode;
import com.example.manager.service.IUserService;
import com.example.manager.utils.JwtUtils;
import com.example.manager.utils.PasswordHelper;
import com.example.manager.utils.UserContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author phos
 * @since 2025-04-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private AuthConfigProperties authConfig;


    @Override
    public void register(UserRegisterDTO dto) {
        // check if user already exists
        boolean usernameExists = this.lambdaQuery().eq(User::getUsername, dto.getUsername()).exists();
        if (usernameExists) {
            throw new BusinessException(ErrorCode.USER_EXISTS);
        }
        boolean emailExists = this.lambdaQuery().eq(User::getEmail, dto.getEmail()).exists();
        if (emailExists) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 使用MapStruct转换DTO到实体
        User user = UserConverter.INSTANCE.toEntity(dto);
        // 确保密码被正确加密
        user.setPassword(PasswordHelper.encryptPassword(dto.getPassword()));
        user.setAvatar("default.png");
        this.save(user);
    }

    @Override
    public TokenVO login(UserLoginDTO dto) {
        // 根据用户名查询用户
        User user = this.lambdaQuery().eq(User::getUsername, dto.getUsername()).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 验证密码
        if (!PasswordHelper.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }

        // 生成token
        return generateToken(user.getId());
    }

    @Override
    public TokenVO refreshToken(String refreshToken) {
        // 验证刷新token
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "刷新token无效或已过期");
        }

        // 获取用户ID
        Integer userId = jwtUtils.getUserId(refreshToken);

        // 查询用户
        User user = getUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 生成新的token
        return generateToken(user.getId());
    }

    @Override
    public User getUserById(Integer id) {
        return this.getById(id);
    }

    @Override
    public void updateUserInfo(UserUpdateDTO dto) {
        Integer userId = UserContext.getUserId();
        // 更新用户信息
        this.lambdaUpdate()
                .eq(User::getId, userId)
                .set(dto.getNickname() != null, User::getNickname, dto.getNickname())
                .set(dto.getAvatar() != null, User::getAvatar, dto.getAvatar())
                .update();
    }

    @Override
    public void changePassword(ChangePasswordDto dto) {
        Integer userId = UserContext.getUserId();
        // 校验密码
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        // 检查旧密码是否匹配
        User user = this.getById(userId);
        if (user == null || !PasswordHelper.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        // 更新密码
        this.lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getPassword, PasswordHelper.encryptPassword(dto.getNewPassword()))
                .update();
    }

    /**
     * 生成token信息
     *
     * @param userId 用户ID
     * @return token信息
     */
    private TokenVO generateToken(Integer userId) {
        // 生成访问token
        String accessToken = jwtUtils.generateAccessToken(userId);

        // 生成刷新token
        String refreshToken = jwtUtils.generateRefreshToken(userId);

        // 返回token信息
        return new TokenVO(
                accessToken,
                refreshToken,
                authConfig.getExpiration(),
                authConfig.getTokenPrefix().trim()
        );
    }
}
