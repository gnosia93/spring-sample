package com.sbk.ssample.test;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sbk.ssample.app.domain.order.Buyer;
import com.sbk.ssample.app.domain.order.BuyerType;
import com.sbk.ssample.app.domain.order.OrderItem;
import com.sbk.ssample.app.domain.order.ShippingInfo;
import com.sbk.ssample.app.service.order.OrderService;
import com.sbk.ssample.ui.order.request.AddOrderRequest;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc               		 // mock object 를 autowire 하기 위해 추가 
public class OrderControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper; 
	@Autowired OrderService orderService;
	
	AddOrderRequest addOrderRequest;

	@Before
	public void prepareAddOrderRequest() {
		List<OrderItem> itemList = new ArrayList<>();
		OrderItem item = new OrderItem();
		item.setItemId(1);
		item.setItemName("item name-1");
		item.setItemCount(1);
		itemList.add(item);
		
		AddOrderRequest addOrderRequest = new AddOrderRequest();
		addOrderRequest.setOrderNo(0);
		addOrderRequest.setTimestamp(Instant.now());
		addOrderRequest.setItemList(itemList);
		addOrderRequest.setBuyer(new Buyer("buyer0", BuyerType.MEMBER, "member0"));
		addOrderRequest.setShippingInfo(new ShippingInfo("kwon", "000-0000-0000", "addr1", "addr2"));
		
		this.addOrderRequest = addOrderRequest;
	}
	
	@Test
	public void testCase() throws Exception {
		String contentBody = objectMapper.writeValueAsString(this.addOrderRequest);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		mockMvc.perform(post("/order/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(contentBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success", equalTo(true)))
			//.andExpect(jsonPath("$.data", is("addOrder()")))
			.andDo(print());
	}
	
	
}
