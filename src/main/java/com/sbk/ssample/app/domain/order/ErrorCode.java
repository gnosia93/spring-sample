package com.sbk.ssample.app.domain.order;

public enum ErrorCode {
	
	PARAM_IS_NULL(1, "input parameter is null"),
	PARAM_IS_EMPTY(2, "input parameter is empty");
	
	int code;
	String message;
	
	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
}
