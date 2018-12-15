package com.sbk.ssample.app.domain.order;

import lombok.Data;

@Data
public class Coupon {
	int couponNo;
	int couponName;
	int discountRatio;
	OrderItem occupiedItem;
}
