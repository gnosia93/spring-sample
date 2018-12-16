package com.sbk.ssample.app.service.order.command;

import java.time.Instant;
import com.sbk.ssample.app.domain.order.Buyer;
import lombok.Data;

@Data
public class CancelOrderCommand {
	
	Buyer buyer;
	long orderId;
	Instant timestatmp;
}
