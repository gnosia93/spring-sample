package com.sbk.ssample.app.domain.order;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Buyer {

	@NotNull
	String buyerId;

	@NotNull
	BuyerType buyerType;
	
	String memberId;
	
}
