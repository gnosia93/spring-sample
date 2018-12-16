package com.sbk.ssample.app.domain.order;

public enum OrderStatus {
	ORDERED("ORDERED"), 
	PAYED("PAYED"), 
	SHIPPED("SHIPPED"), 
	COMPLETE("DELIVERY OK"), 
	CANCELED("CANCELED");
	
	String code;
	
	private OrderStatus(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	
}
