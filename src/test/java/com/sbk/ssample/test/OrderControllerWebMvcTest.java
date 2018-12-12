package com.sbk.ssample.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbk.ssample.app.domain.order.Item;
import com.sbk.ssample.app.service.order.OrderService;
import com.sbk.ssample.app.service.order.command.AddOrderCommand;
import com.sbk.ssample.base.CommandResult;
import com.sbk.ssample.ui.order.controller.OrderController;
import com.sbk.ssample.ui.order.request.AddOrderRequest;
import com.sbk.ssample.ui.order.request.mapper.AddOrderRequestMapper;

import fr.xebia.extras.selma.Selma;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerWebMvcTest {
	
	@Autowired
	MockMvc mvc;
	
	/* 
	 * @WebMvcTest 인 경우 MVC 와 관련된 빈만 생성한다. Service, Repository, Component 와 같은 빈은 생성 하지 않는다. 
	 * @MockBean 어노테이션을 이용하여 서비스 Mock 객체를 주입하고 있다.
	 */
	@MockBean			 
	OrderService orderService;
	
	/*
	 * Selma Mapper 역시 로딩하지 않는다.. Test Case 에서 Selma Mapper 를 강제로 주입하는 방법은 ?  
	 */
	@MockBean
	AddOrderRequestMapper orderRequestMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
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
	public void testOrder() throws Exception {
		AddOrderRequest orderRequest = getAddOrderRequest();
		
		AddOrderRequestMapper mapper = Selma.builder(AddOrderRequestMapper.class).build();
		AddOrderCommand orderCommand = mapper.asAddOrderCommand(orderRequest);
		// doNothing().when(orderService).order();
		when(orderRequestMapper.asAddOrderCommand(orderRequest)).thenReturn(mapper.asAddOrderCommand(orderRequest));
		when(orderService.order(orderCommand)).thenReturn(CommandResult.success("call by mock"));
		
		
		mvc.perform(
				post("/order/add")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(objectMapper.writeValueAsString(orderCommand)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success", equalTo(true)))
				.andExpect(jsonPath("$.data", is("call by mock")))
				.andDo(print());

		// verify if order method is called
		verify(orderService).order(orderCommand);
		
	}

	
	
}
