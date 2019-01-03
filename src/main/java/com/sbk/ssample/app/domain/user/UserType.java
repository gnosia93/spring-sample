package com.sbk.ssample.app.domain.user;

public enum UserType {
	MEMBER(1),
	NON_MEMBER(2);
	
	int code;
	
	private UserType(int code) {
		this.code = code;
	}
}
