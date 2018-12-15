package com.sbk.ssample.infra.order.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="tb_order_item")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	long orderId;
	String buyerId;
	String itemName;
	int itemCount;
	int itemPrice;
	
}
