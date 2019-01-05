package com.sbk.ssample.ui.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.sbk.ssample.ui.user.annotation.PasswordMatches;
import com.sbk.ssample.ui.user.request.AddUserRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		AddUserRequest user = (AddUserRequest)value;
		return user.getPassword().equals(user.getMatchingPassword());
	}

}
