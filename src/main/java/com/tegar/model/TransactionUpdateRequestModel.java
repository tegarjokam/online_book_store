package com.tegar.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tegar.entity.Transaction.TransactionStatus;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionUpdateRequestModel {
	
	@NotNull
	private Integer transactionId;
	
	@NotNull
	private TransactionStatus transactionStatus;
	
	private String receiptImageUrl;

}
