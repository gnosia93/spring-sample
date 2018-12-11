package com.sbk.springsample.order.domain;

import org.springframework.stereotype.Component;

import com.sbk.springsample.order.domain.exception.DomainException;

@Component
public class OrderImpl implements Order {

	@Override
	public void order() {
		
		/*
		 * error-code / error-message /disable printstack trace. ??
		 * 
		 */
		throw new DomainException();
	}
	
}
