package com.sbk.ssample.ui.order.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.sbk.ssample.ui.helper.CommandResult;

public interface Controller {
	
	default ResponseEntity<CommandResult> ok(Object data) {
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(CommandResult.success(data));
	}
	
	default ResponseEntity<CommandResult> ok() {
		return this.ok("");
	}

	default ResponseEntity<CommandResult> success(Object data) {
		return this.ok(data);
	}
	
	default ResponseEntity<CommandResult> success() {
		return this.ok("");
	}
	
	
	
}
