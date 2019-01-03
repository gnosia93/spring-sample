package com.sbk.ssample.infra.order.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sbk.ssample.app.domain.order.repository.RefundRepository;
import com.sbk.ssample.infra.gateway.GateWay;
import com.sbk.ssample.ui.helper.CommandResult;

@Component
public class RefundRepositoryImpl implements RefundRepository {

	@Autowired GateWay restGateWay;
	
	
	@Override
	public void refund(long orderId) {
		
		restGateWay.getRestTemplate()
				   .getForEntity("http://localhost:8080/refund/1", CommandResult.class);
		
	}

}
