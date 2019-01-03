package com.sbk.ssample.ui.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbk.ssample.app.domain.user.Address;
import com.sbk.ssample.app.domain.user.Gender;
import com.sbk.ssample.app.service.user.UserService;
import com.sbk.ssample.app.service.user.command.AddUserCommand;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

	final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/add")
	public void getUser() {
		//AddUserRequest --> AddUserCommand --> User Domain Entity --> repository(I)
		
		AddUserCommand addUserCommand = new AddUserCommand();
		addUserCommand.setId("mongo-id-test-01");
		addUserCommand.setGender(Gender.FEMAIL);
		addUserCommand.setPassword("password");
		addUserCommand.setGender(Gender.MAIL);
		addUserCommand.setName("name");
		addUserCommand.setAddress(new Address("00000", "서울시", "강동구"));
		
		userService.add(addUserCommand);
		log.info("UserController is called..");
	}
	
	
}
