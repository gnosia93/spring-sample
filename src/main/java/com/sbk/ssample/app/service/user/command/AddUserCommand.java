package com.sbk.ssample.app.service.user.command;

import com.sbk.ssample.app.domain.user.Address;
import com.sbk.ssample.app.domain.user.Gender;
import com.sbk.ssample.app.domain.user.UserType;

import lombok.Data;

@Data
public class AddUserCommand {

	String id;

	UserType userType;
	
	String name;
	
	String password;

	Gender gender;
	
	Address address;
	
}
