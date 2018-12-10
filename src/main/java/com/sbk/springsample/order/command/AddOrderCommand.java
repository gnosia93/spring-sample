package com.sbk.springsample.order.command;

import java.time.Instant;
import java.util.List;

import com.sbk.springsample.order.domain.Item;

import lombok.Data;

@Data
public class AddOrderCommand {

	long orderNo;
	List<Item> itemList;
	Instant timestamp;
	// Buyer ??
}
