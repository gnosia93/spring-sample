package com.sbk.ssample.app.domain.order;

import java.util.List;

import org.springframework.util.StringUtils;

import com.sbk.ssample.app.domain.order.exception.DomainException;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;



/*
 * Order 도메인 객체
 * 도메인 객체는 Service 객체에 의해 생성 / 변경되며, Repository 객체에 의해 영속성이 유지된다.  
 */

// @Component
// @Scope("prototype")
@Data
public class Order {
	long orderId;
	Buyer buyer;
	List<OrderItem> itemList;
	ShippingInfo shippingInfo;

	OrderStatus orderStatus;
	int totalPrice;

	public Order(Buyer buyer, List<OrderItem> itemList, ShippingInfo shippingInfo) {
		
		this.buyer = buyer;
		this.itemList = itemList;
		this.shippingInfo = shippingInfo;

		setOrderStatus(OrderStatus.ORDERED);
		assertConstructor();
	}
	
	void assertConstructor() {
		// parameter 값 점검..
	}
	
	boolean assertShippingInfo(ShippingInfo shippingInfo) {
		if(shippingInfo == null ||
		   StringUtils.isEmpty(shippingInfo.getReceiverName()) ||
		   StringUtils.isEmpty(shippingInfo.getReceiverPhoneNumber()) )
			return true;
		
		return false;
	}
	
	void setShippingInfo(ShippingInfo shippingInfo) {
		this.shippingInfo = shippingInfo;
	}
	
	void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
	boolean isChangeableShippingInfo() {
		if(this.orderStatus == OrderStatus.ORDERED)
			return true;
		return false;
	}
	

	// below is public..
	
	public void changeShippingInfo(ShippingInfo shippingInfo) {
		if(assertShippingInfo(shippingInfo))
			throw new DomainException(ErrorCode.PARAM_IS_NULL);
		
		if(!isChangeableShippingInfo())
			throw new DomainException(ErrorCode.ORDER_SHIPPINGINFO_CHANGE_ERROR, "이미 배송중");
	
		setShippingInfo(shippingInfo);
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
