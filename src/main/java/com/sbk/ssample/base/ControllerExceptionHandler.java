package com.sbk.ssample.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbk.ssample.app.domain.order.exception.DomainException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ResponseBody
	@ExceptionHandler(value = {DomainException.class})
	public ResponseEntity<CommandResult> handleDomainException(
			HttpServletRequest request, HttpServletResponse response, Exception e) {
	
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(CommandResult.fail(-1, e.getMessage()));
	}
}
