package com.sbk.ssample.ui.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sbk.ssample.ui.helper.CommandResult;

@RestController
public class RefundController implements Controller {

	@GetMapping("/refund/{id}")
	public ResponseEntity<CommandResult> refuncd(@PathVariable int id) {

		try {
			Thread.sleep(6000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		Map<String, String> result = new HashMap<>();
		result.put("orderId:", String.valueOf(id));
		result.put("result", "ok");
		
		return ok(CommandResult.success(result));
	}
	
}
