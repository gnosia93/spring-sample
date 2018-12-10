package com.sbk.springsample.order.domain;

import lombok.Data;

@Data
public class Item {
	int itemId;
	String itemName;
	int itemCount;
}
