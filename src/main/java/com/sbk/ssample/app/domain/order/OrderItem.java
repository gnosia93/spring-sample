package com.sbk.ssample.app.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	long itemId;
	String itemName;
	int itemCount;
	int itemPrice;
}
