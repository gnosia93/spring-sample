package com.sbk.ssample.app.service.user;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbk.ssample.app.domain.user.User;
import com.sbk.ssample.app.domain.user.repository.UserRepository;
import com.sbk.ssample.app.service.user.command.AddUserCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void add(@NotNull AddUserCommand addUserCommand) {
		
		User user = User.builder()
				.id(addUserCommand.getId())
				.password(addUserCommand.getPassword())
				.name(addUserCommand.getName())
				.gender(addUserCommand.getGender())
				.userType(addUserCommand.getUserType())
				.address(addUserCommand.getAddress())
				.email(addUserCommand.getEmail())
				.build();
		
		userRepository.addUser(user);
	}
	
	public Optional<User> findById(String id) {
		return userRepository.findById(id);
	}
	
}
