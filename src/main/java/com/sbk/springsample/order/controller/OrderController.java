package com.sbk.springsample.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sbk.springsample.order.command.AddOrderCommand;
import com.sbk.springsample.order.domain.Buyer;

@RestController
@RequestMapping(value="/order")
public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/add", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<?> addOrder(AddOrderCommand addOrderCommand) {
	
		System.out.println(addOrderCommand);
		
		
		
		return ResponseEntity
			.ok()
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body("...");
	}
	
	
	
}
