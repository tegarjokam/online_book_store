package com.tegar.model;

import lombok.Setter;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartRequestModel {
	
	@NotNull
	private Integer userId;
	
	@NotNull
	private Integer bookId;
	

}
