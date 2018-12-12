package com.sbk.ssample.app.domain.order.exception;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 8490516509816427963L;

	public DomainException() {
		super("base of domain exception");
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
