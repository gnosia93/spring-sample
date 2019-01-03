package com.sbk.ssample.app.domain.user;

public enum Gender {
	MAIL("M"),
	FEMAIL("F"),
	DEFAULT("D");
	
	String code;
	
	private Gender(String code) {
		this.code = code;
	}
	
	public static Gender fromCode(String code) {
		if(code != null) {
			switch(code) {
			case "M":
				return Gender.MAIL;
			case "F":
				return Gender.FEMAIL;
			}
		}	
		return Gender.DEFAULT;
	}
	
	
}
