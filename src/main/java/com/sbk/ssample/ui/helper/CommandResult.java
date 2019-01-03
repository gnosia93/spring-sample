package com.sbk.ssample.ui.helper;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "timestamp", "success", "errorCode", "errorMessage", "data" })
public class CommandResult<T> {

	Instant timestamp = Instant.now();
	boolean success;
	int errorCode;
	String errorMessage;
	T data;

	private CommandResult() {}
	
	public static<T> CommandResult<T> success() {
		return success(null);
	}

	public static<T> CommandResult<T> success(T data) {
		CommandResult<T> result = new CommandResult<>();
		result.setSuccess(true);
		result.setErrorMessage("");
		result.setData(data);
		return result;
	}

	public static<T> CommandResult<T> fail(int errorCode) {
		return fail(errorCode, "", null);
	}
	
	public static<T> CommandResult<T> fail(int erorrCode, String errorMessage) {
		return fail(erorrCode, errorMessage, null);
	}
	
	public static<T> CommandResult<T> fail(int errorCode, String errorMessage, T data) {
		CommandResult<T> result = new CommandResult<>();
		result.setSuccess(false);
		result.setErrorCode(errorCode);
		result.setErrorMessage(errorMessage);
		result.setData(data);
		return result;
	}
	
	
	
}
