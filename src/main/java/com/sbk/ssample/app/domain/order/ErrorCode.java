package com.sbk.ssample.app.domain.order;

public enum ErrorCode {
	
	PARAM_IS_NULL(1, "input parameter is null"),
	PARAM_IS_EMPTY(2, "input parameter is empty"),
	
	
	ORDER_NO_EXIST(100, "order doesn't exist"),
	ORDER_CANCEL_IMPOSSIBLE(101, "order cannot be canceled"), 
	ORDER_BUYER_ID_MISSMATCH(102, "orderer id is missmatch.."),
	
	
	ORDER_ITEM_COUNT_ZERO(11, "order item count must bigger than zero"),
	ORDER_SHIPPINGINFO_CHANGE_ERROR(12, "can't chnage cause of order status ");
	
	
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
