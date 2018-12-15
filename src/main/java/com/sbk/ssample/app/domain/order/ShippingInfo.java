package com.sbk.ssample.app.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfo {
	String receiverName;
	String receiverPhoneNumber;
	String receiverAddr1;
	String receiverAddr2;
}
