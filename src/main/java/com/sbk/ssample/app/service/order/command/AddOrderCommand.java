package com.sbk.ssample.app.service.order.command;

import java.time.Instant;
import java.util.List;

import com.sbk.ssample.app.domain.order.Coupon;
import com.sbk.ssample.app.domain.order.Item;

import lombok.Data;

@Data
public class AddOrderCommand {

	Instant timestamp;
	long orderNo;
	List<Item> itemList;
	List<Coupon> couponList;
	
	// Buyer ??
}
