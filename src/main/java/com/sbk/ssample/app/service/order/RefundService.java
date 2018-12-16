package com.sbk.ssample.app.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbk.ssample.app.domain.order.repository.RefundRepository;

@Service
public class RefundService {

	RefundRepository refundRepository;
		
	@Autowired
	public RefundService(RefundRepository refundRepository) {
		this.refundRepository = refundRepository;
	}
	
	public void refund(long orderId) {
		
		refundRepository.refund(orderId);
		
	}

}
