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
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbk.springsample.order.domain.Item;
import com.sbk.springsample.order.service.OrderService;
import com.sbk.springsample.ui.order.command.AddOrderCommand;


@RunWith(SpringJUnit4ClassRunner.class)
// @WebMvcTest
@SpringBootTest
public class OrderControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper; 
	@Autowired OrderService orderService;
	
	
	private AddOrderCommand makeAddOrderCommand() {
		List<Item> itemList = new ArrayList<>();
		Item item = new Item();
		item.setItemId(1);
		item.setItemName("item name-1");
		item.setItemCount(1);
		itemList.add(item);
		
		AddOrderCommand addOrderCommand = new AddOrderCommand();
		addOrderCommand.setOrderNo(0);
		addOrderCommand.setTimestamp(Instant.now());
		addOrderCommand.setItemList(itemList);
		
		return addOrderCommand;
	}
	
	@Test
	public void testCase() throws Exception {
		AddOrderCommand addOrderCommand = makeAddOrderCommand();
		String contentBody = objectMapper.writeValueAsString(addOrderCommand);
		
		mockMvc.perform(post("/order/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(contentBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success", equalTo(true)))
			.andExpect(jsonPath("$.data", is("addOrder()")))
			.andDo(print());
	}
	
	
}
