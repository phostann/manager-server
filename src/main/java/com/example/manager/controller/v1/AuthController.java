package com.example.manager.controller.v1;

import com.example.manager.domain.dto.auth.ChangePasswordDto;
import com.example.manager.domain.dto.auth.UserLoginDTO;
import com.example.manager.domain.dto.auth.TokenRefreshDTO;
import com.example.manager.domain.dto.auth.UserRegisterDTO;
import com.example.manager.domain.vo.auth.TokenVO;
import com.example.manager.response.R;
import com.example.manager.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Resource
    private IUserService userService;

    /**
     * 用户注册
     *
     * @param dto 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return R.ok();
    }

    /**
     * 用户登录
     *
     * @param dto 登录信息
     * @return token信息
     */
    @PostMapping("/login")
    public R<TokenVO> login(@Valid @RequestBody UserLoginDTO dto) {
        TokenVO tokenVo = userService.login(dto);
        return R.ok(tokenVo);
    }

    /**
     * 刷新token
     *
     * @param dto 刷新token信息
     * @return 新的token信息
     */
    @PostMapping("/refresh")
    public R<TokenVO> refresh(@Valid @RequestBody TokenRefreshDTO dto) {
        TokenVO tokenVo = userService.refreshToken(dto.getRefreshToken());
        return R.ok(tokenVo);
    }

    /**
     * 修改密码
     *
     * @param dto 修改密码信息
     * @return 修改结果
     */
    @PostMapping("/change-password")
    public R<Void> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
        return R.ok();
    }

}
