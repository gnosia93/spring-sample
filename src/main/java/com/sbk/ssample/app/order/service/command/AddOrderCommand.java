package com.sbk.ssample.app.order.service.command;

import java.time.Instant;
import java.util.List;

import com.sbk.ssample.domain.order.Coupon;
import com.sbk.ssample.domain.order.Item;

import lombok.Data;

@Data
public class AddOrderCommand {

	Instant timestamp;
	long orderNo;
	List<Item> itemList;
	List<Coupon> couponList;
	
	// Buyer ??
}
