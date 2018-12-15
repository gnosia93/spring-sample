package com.sbk.ssample.ui.order.request;

import java.time.Instant;
import java.util.List;

import com.sbk.ssample.app.domain.order.Buyer;
import com.sbk.ssample.app.domain.order.Coupon;
import com.sbk.ssample.app.domain.order.OrderItem;
import com.sbk.ssample.app.domain.order.ShippingInfo;

import lombok.Data;

@Data
public class AddOrderRequest {

	Instant timestamp;
	long orderNo;
	List<OrderItem> itemList;
	List<Coupon> couponList;
	Buyer buyer;
	ShippingInfo shippingInfo;
}
