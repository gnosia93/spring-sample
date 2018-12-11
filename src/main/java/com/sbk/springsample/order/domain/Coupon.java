package com.sbk.springsample.order.domain;

import lombok.Data;

@Data
public class Coupon {
	int couponNo;
	int couponName;
	int discountRatio;
	Item occupiedItem;
}
