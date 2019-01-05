package com.sbk.ssample.infra.user.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sbk.ssample.app.domain.user.User;
import com.sbk.ssample.app.domain.user.repository.UserRepository;
import com.sbk.ssample.infra.order.jpa.entity.UserEntity;
import com.sbk.ssample.infra.order.jpa.repository.UserJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserRepositoryImpl implements UserRepository {

	UserJpaRepository userJpaRepository;
	
	@Autowired
	public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}
	
	@Override
	public void addUser(User user) {
		
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setPassword(user.getPassword());
		userEntity.setAddress(user.getAddress());
		userEntity.setGender(user.getGender());
		userEntity.setName(user.getName());
		userEntity.setUserType(user.getUserType());
		userEntity.setEmail(user.getEmail());
		
		userJpaRepository.save(userEntity);
	}

	@Override
	public Optional<User> findById(String id) {
		return null;
	}

}
