package com.sbk.ssample.app.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Address {

	String zipCode;
	String addr1;
	String addr2;
	
}
