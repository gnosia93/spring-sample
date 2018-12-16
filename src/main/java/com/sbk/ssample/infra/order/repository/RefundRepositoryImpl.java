package com.sbk.ssample.infra.order.repository;

import org.springframework.stereotype.Component;

import com.sbk.ssample.app.domain.order.repository.RefundRepository;

@Component
public class RefundRepositoryImpl implements RefundRepository {

	@Override
	public void refund(long orderId) {
		
		System.out.println("infra..refundrepository.. we use resttemplate..");
		
		
	}

}
