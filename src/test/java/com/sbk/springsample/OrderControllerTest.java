package com.sbk.springsample;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbk.ssample.app.order.service.OrderService;
import com.sbk.ssample.app.order.service.command.AddOrderCommand;
import com.sbk.ssample.domain.order.Item;
import com.sbk.ssample.ui.order.request.AddOrderRequest;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc               		 // mock object 를 autowire 하기 위해 추가 
public class OrderControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper; 
	@Autowired OrderService orderService;
	
	
	private AddOrderRequest getAddOrderRequest() {
		List<Item> itemList = new ArrayList<>();
		Item item = new Item();
		item.setItemId(1);
		item.setItemName("item name-1");
		item.setItemCount(1);
		itemList.add(item);
		
		AddOrderRequest addOrderRequest = new AddOrderRequest();
		addOrderRequest.setOrderNo(0);
		addOrderRequest.setTimestamp(Instant.now());
		addOrderRequest.setItemList(itemList);
		
		return addOrderRequest;
	}
	
	@Test
	public void testCase() throws Exception {
		AddOrderRequest orderRequest = getAddOrderRequest();
		String contentBody = objectMapper.writeValueAsString(orderRequest);
		
		mockMvc.perform(post("/order/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(contentBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success", equalTo(true)))
			.andExpect(jsonPath("$.data", is("addOrder()")))
			.andDo(print());
	}
	
	
}
