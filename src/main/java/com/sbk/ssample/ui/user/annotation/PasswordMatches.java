package com.sbk.ssample.ui.user.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.sbk.ssample.ui.user.validator.PasswordMatchesValidator;

@Target({
    ElementType.TYPE, 					// 타입 선언시
    ElementType.ANNOTATION_TYPE 		// 어노테이션 타입 선언시
})
@Retention(RetentionPolicy.RUNTIME) 	// 컴파일 이후에도 JVM에 의해서 참조가 가능합니다.
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
	String message() default "패스워드가 일치하지 않습니다.";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}
