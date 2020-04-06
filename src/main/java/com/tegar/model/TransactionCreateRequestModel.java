package com.tegar.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionCreateRequestModel {
	
	@NotNull
	private Integer userId;
	
	@NotNull
	private Set<Integer> cartDetailIds;

}
