package com.sbk.ssample.app.domain.order.exception;

import com.sbk.ssample.app.domain.order.ErrorCode;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 8490516509816427963L;	

	ErrorCode errorCode;
	String additionalMessage;
	
	public DomainException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public DomainException(ErrorCode errorCode, Throwable throwable) {
		this.errorCode = errorCode;
		
	}
	
	// custom..
	public DomainException(ErrorCode errorCode, String additionalMessage) {
		this.errorCode = errorCode;
		this.additionalMessage = additionalMessage;
	}
	
	
	public int getCode() {
		return this.errorCode.getCode();
	}

	public String getMessage() {
		return this.errorCode.getMessage() + " " + this.additionalMessage;
	}
	
	/*
	 * 스택트레이스를 disable 한다.
	 * @see java.lang.Throwable#fillInStackTrace()
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}       
}
