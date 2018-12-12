package com.sbk.ssample.ui.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbk.ssample.app.service.order.OrderService;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;
import com.sbk.ssample.ui.order.request.AddOrderRequest;
import com.sbk.ssample.ui.order.request.mapper.AddOrderRequestMapper;

@RestController
@RequestMapping(value="/order")
public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	OrderService orderService;
	AddOrderRequestMapper orderRequestMapper;
	
	@Autowired
	public OrderController(OrderService service, AddOrderRequestMapper orderRequestMapper) {
		this.orderService = service;
		this.orderRequestMapper = orderRequestMapper;
	}
	
	
	/*
	 * 주문한다.  
	 */
	@PostMapping("/add")
	public ResponseEntity<CommandResult> order(@RequestBody AddOrderRequest addOrderRequest) {
		
		AddOrderCommand command = this.orderRequestMapper.asAddOrderCommand(addOrderRequest);
		
		CommandResult commandResult = orderService.order(command);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(commandResult);
	}
	
	
	
}
