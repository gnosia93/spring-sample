package com.sbk.ssample.app.domain.order;

public enum BuyerType {
	MEMBER(1),
	NON_MEMBER(2);
	
	int code;
	
	private BuyerType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}

}
