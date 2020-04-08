package com.tegar.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tegar.entity.Shipment.Courier;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentCreateRequestModel {
	
	@NotNull
	private Integer userId;
	
	@NotNull
	private Integer transactionId;
	
	@NotNull
	private Courier courier;

}
