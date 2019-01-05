package com.sbk.ssample.infra.order.jpa.entity;

import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.sbk.ssample.app.domain.user.Address;
import com.sbk.ssample.app.domain.user.Gender;
import com.sbk.ssample.app.domain.user.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
@Data
public class UserEntity {
	
	@Id
	String id;

	UserType userType;
	
	String name;
	
	Gender gender;
	
	Address address;
	
}
