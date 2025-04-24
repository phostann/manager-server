package com.example.manager.converter;

import com.example.manager.domain.dto.auth.UserRegisterDTO;
import com.example.manager.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct用户对象转换接口
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * RegisterDto转UserEntity
     * 注意：密码字段会在Service层单独处理
     */
    User toEntity(UserRegisterDTO dto);
} 