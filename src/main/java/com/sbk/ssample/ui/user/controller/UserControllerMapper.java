package com.sbk.ssample.ui.user.controller;

import com.sbk.ssample.app.domain.user.Address;
import com.sbk.ssample.app.domain.user.Gender;
import com.sbk.ssample.app.domain.user.UserType;
import com.sbk.ssample.app.service.user.command.AddUserCommand;
import com.sbk.ssample.ui.user.request.AddUserRequest;

public interface UserControllerMapper {
	
	default AddUserCommand asAddUserCommand(AddUserRequest addUserRequest) {
		AddUserCommand addUserCommand = new AddUserCommand();
		addUserCommand.setId(addUserRequest.getFirstName() + addUserRequest.getLastName());
		addUserCommand.setPassword(addUserRequest.getPassword());
		addUserCommand.setName(addUserRequest.getFirstName() + " " + addUserRequest.getLastName());
		addUserCommand.setEmail(addUserRequest.getEmail());
		
		addUserCommand.setAddress(new Address("00000", "서울시", "강동구"));
		addUserCommand.setGender(Gender.FEMAIL);
		addUserCommand.setUserType(UserType.MEMBER);
		return addUserCommand;
	}

}
