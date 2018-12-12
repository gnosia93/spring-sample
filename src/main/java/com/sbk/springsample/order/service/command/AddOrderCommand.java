package com.sbk.springsample.order.service.command;

import java.time.Instant;
import java.util.List;

import com.sbk.springsample.order.domain.Coupon;
import com.sbk.springsample.order.domain.Item;

import lombok.Data;

@Data
public class AddOrderCommand {

	Instant timestamp;
	long orderNo;
	List<Item> itemList;
	List<Coupon> couponList;
	
	// Buyer ??
}
