package com.tegar.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.tegar.util.FieldValueMatchValidator;

@Constraint(validatedBy = FieldValueMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldValueMatch {
	
	String message() default "fields values don't match!"; 
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {}; 
	
	String field();
	    
	String fieldMatch();
	
	@Target({ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	@interface List {
		FieldValueMatch[] value();
	}

}
