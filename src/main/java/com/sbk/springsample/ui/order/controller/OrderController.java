package com.sbk.springsample.ui.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbk.springsample.base.CommandResult;
import com.sbk.springsample.order.service.OrderService;
import com.sbk.springsample.ui.order.command.AddOrderCommand;

@RestController
@RequestMapping(value="/order")
public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	OrderService orderService;
	
	@Autowired
	public OrderController(OrderService service) {
		this.orderService = service;
	}
	
	
	/*
	 * 주문한다.  
	 */
	@PostMapping("/add")
	public ResponseEntity<CommandResult> order(@RequestBody AddOrderCommand addOrderCommand) {
		
		orderService.order();
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(CommandResult.success("addOrder()"));
	}
	
	
	
}
