package com.sbk.ssample.app.domain.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfo {
	String reciverName;
	String reciverPhoneNumber;
	String recieverAddr1;
	String receiverAddr2;
}
