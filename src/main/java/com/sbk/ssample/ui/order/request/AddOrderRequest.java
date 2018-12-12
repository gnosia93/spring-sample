package com.sbk.ssample.ui.order.request;

import java.time.Instant;
import java.util.List;

import com.sbk.ssample.app.domain.order.Coupon;
import com.sbk.ssample.app.domain.order.Item;

import lombok.Data;

@Data
public class AddOrderRequest {

	Instant timestamp;
	long orderNo;
	List<Item> itemList;
	List<Coupon> couponList;
	
	// Buyer ??
}
