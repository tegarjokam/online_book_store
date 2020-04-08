package com.tegar.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tegar.entity.Shipment.ShipmentStatus;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentUpdateRequestModel {

	@NotNull
	private Integer shipmentId;
	
	private String trackingNumber;
	
	@NotNull
	private ShipmentStatus shipmentStatus;
}
