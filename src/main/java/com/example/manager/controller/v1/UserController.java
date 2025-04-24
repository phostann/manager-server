package com.example.manager.controller.v1;

import com.example.manager.domain.dto.user.UserUpdateDTO;
import com.example.manager.entity.User;
import com.example.manager.exception.BusinessException;
import com.example.manager.response.ErrorCode;
import com.example.manager.response.R;
import com.example.manager.service.IUserService;
import com.example.manager.utils.UserContext;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/profile")
    public R<User> getCurrentUser() {
        Integer userId = UserContext.getUserId();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        // 清除敏感信息
        user.setPassword(null);
        return R.ok(user);
    }

    /**
     * 更新用户信息
     *
     * @param dto
     * @return
     */
    @PutMapping("/update")
    public R<Void> updateUser(@Validated @RequestBody UserUpdateDTO dto) {
        userService.updateUserInfo(dto);
        // 用户更新逻辑
        return R.ok();
    }
}
