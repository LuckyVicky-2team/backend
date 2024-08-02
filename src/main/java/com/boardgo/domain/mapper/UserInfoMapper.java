package com.boardgo.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.boardgo.domain.user.controller.dto.SignupRequest;
import com.boardgo.domain.user.entity.UserInfoEntity;

@Mapper
public interface UserInfoMapper {
	UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

	UserInfoEntity toUserInfoEntity(SignupRequest signupRequest);
}