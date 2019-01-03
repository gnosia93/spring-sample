package com.sbk.ssample.app.domain.user.repository;

import java.util.Optional;

import com.sbk.ssample.app.domain.user.User;

public interface UserRepository {

	public void addUser(User user);

	public Optional<User> findById(String id);
	
}
