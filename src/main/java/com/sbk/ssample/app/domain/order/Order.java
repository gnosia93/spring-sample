package com.sbk.ssample.app.domain.order;

import java.util.List;

import org.springframework.util.StringUtils;

import com.sbk.ssample.app.domain.order.exception.DomainException;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;



/*
 * Order 객체
 * 도메인 객체는 스프링 Bean으로 관리하지 않는다. 
 */

// @Component
// @Scope("prototype")
@Data
@RequiredArgsConstructor
public class Order {
	long orderId;
	
	@NonNull
	Buyer buyer;
	@NonNull
	List<OrderItem> itemList;
	@NonNull
	ShippingInfo shippingInfo;

	OrderStatus orderStatus;
	int totalPrice;

	/*
	 * C  - new Order() --> repository save.
	 * R  - repository find 
	 * U  - repository find / change order object / save
	 * D  - repository delete
	 */
	private boolean assertShippingInfo(ShippingInfo shippingInfo) {
		if(shippingInfo == null ||
		   StringUtils.isEmpty(shippingInfo.getReceiverName()) ||
		   StringUtils.isEmpty(shippingInfo.getReceiverPhoneNumber()) )
			return true;
		
		return false;
	}
	
	public void changeShippingInfo(ShippingInfo shippingInfo) {
		if(assertShippingInfo(shippingInfo))
			throw new DomainException(ErrorCode.PARAM_IS_NULL);
		
		this.shippingInfo = shippingInfo;
	}
	
	public int getTotalPrice() {
		return itemList.stream()
				.map(item -> { return item.getItemCount() * item.getItemPrice(); })
				.mapToInt(i -> i).sum();
	}
	
	public int getItemCount( ) {
		return itemList.size();
	}
	
	public void cancelOrder() {
		
	}
	
}
