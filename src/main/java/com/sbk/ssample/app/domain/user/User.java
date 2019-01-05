package com.sbk.ssample.app.domain.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
public class User {
	
	String id;

	UserType userType;
	
	String name;
	
	Gender gender;
	
	Address address;
	
	/*
	public boolean isMember() {
		return userType == UserType.MEMBER ? true: false;
	}
	*/
	
	public String getId() {
		if(userType == UserType.NON_MEMBER)
			return UUID.randomUUID().toString();
		
		return this.id;
	}

}
