package com.sbk.ssample.app.domain.order;

import java.util.List;

import org.springframework.util.StringUtils;

import com.sbk.ssample.app.domain.order.exception.DomainException;
import com.sbk.ssample.app.service.order.RefundService;

import lombok.Builder;
import lombok.Data;



/*
 * Order 도메인 객체
 * 도메인 객체는 Service 객체에 의해 생성 / 변경되며, Repository 객체에 의해 영속성이 유지된다.  
 */

// @Component
// @Scope("prototype")
@Builder
@Data
public class Order {
	long orderId;
	Buyer buyer;
	List<OrderItem> itemList;
	ShippingInfo shippingInfo;

	OrderStatus orderStatus;
	int totalPrice;

	public Order(long orderId, Buyer buyer, List<OrderItem> itemList, 
			ShippingInfo shippingInfo, OrderStatus orderStatus,
			int totalPrice) {
		this.orderId = orderId;
		this.buyer = buyer;
		this.itemList = itemList;
		this.shippingInfo = shippingInfo;
		this.orderStatus = orderStatus;
		this.totalPrice = totalPrice;

	//	setOrderStatus(OrderStatus.ORDERED);
		assertConstructorPrameters();
	}
	
	
	public Order(Buyer buyer, List<OrderItem> itemList, ShippingInfo shippingInfo) {
		this(0, buyer, itemList, shippingInfo, OrderStatus.ORDERED, 0);
	}
	
	void assertConstructorPrameters() {
		if(this.buyer == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL, "buyer is null");
		
		if(this.itemList == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL, "itemList is null");
	
		if(this.shippingInfo == null)
			throw new DomainException(ErrorCode.PARAM_IS_NULL, "shippingInfo is null");
	
		
		if(this.itemList.size() == 0)
			throw new DomainException(ErrorCode.ORDER_ITEM_COUNT_ZERO);
		
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
	
	
	void assertOrderCancelable() {
		if( this.orderStatus != OrderStatus.ORDERED )
			throw new DomainException(ErrorCode.ORDER_CANCEL_IMPOSSIBLE, this.orderStatus.toString());
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
	
	public void cancel(RefundService refundService) {
		assertOrderCancelable();
		setOrderStatus(OrderStatus.CANCELED);
		
		refundStarted();
		try {
			// 외부 서비스를 호출한다.  
			refundService.refund(this.getOrderId());
			refundCompleted();
		}
		catch(Exception e) {
			e.printStackTrace();
			refundFailed();
			throw e;
		}
	}
	
	public void refundStarted() {
		
	}
	
	public void refundCompleted() {
		
	}
	
	public void refundFailed() {
		
	}
	
}
