package com.sbk.ssample.infra.user.mapper;

import com.sbk.ssample.app.domain.user.User;
import com.sbk.ssample.infra.order.jpa.entity.UserEntity;

import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

@Mapper(withIoC = IoC.SPRING)
public interface UserEntityMapper {

	UserEntity asUserEntity(User user);
	
	// User asUser(UserEntity userEntity);
}
