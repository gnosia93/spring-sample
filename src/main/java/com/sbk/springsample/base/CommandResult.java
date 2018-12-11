package com.sbk.springsample.base;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "timestamp", "success", "errorCode", "errorMessage", "data" })
public class CommandResult {

	Instant timestamp = Instant.now();
	boolean success;
	int errorCode;
	String errorMessage;
	Object data;

	private CommandResult() {}
	
	public static CommandResult success() {
		return success("");
	}

	public static CommandResult success(String data) {
		CommandResult result = new CommandResult();
		result.setSuccess(true);
		result.setErrorMessage("");
		result.setData(data);
		return result;
	}

	public static CommandResult fail(int errorCode) {
		return fail(errorCode, "", "");
	}
	
	public static CommandResult fail(int erorrCode, String errorMessage) {
		return fail(erorrCode, errorMessage, "");
	}
	
	public static CommandResult fail(int errorCode, String errorMessage, String data) {
		CommandResult result = new CommandResult();
		result.setSuccess(false);
		result.setErrorCode(errorCode);
		result.setErrorMessage(errorMessage);
		result.setData(data);
		return result;
	}
	
	
	
}
