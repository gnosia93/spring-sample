package com.sbk.ssample.app.domain.order;

public enum ErrorCode {
	
	PARAM_IS_NULL(1, "input parameter is null"),
	PARAM_IS_EMPTY(2, "input parameter is empty"),
	ORDER_SHIPPINGINFO_CHANGE_ERROR(11, "can't chnage cause of order status ");
	
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
