package com.tegar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tegar.entity.Shipment.Courier;
import com.tegar.entity.Shipment.ShipmentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentModel extends PersistenceModel {
	
	private String trackingNumber;
	private String address;
	
	private TransactionModel transactionModel;
	private UserModel userModel;
	
	private ShipmentStatus shipmentStatus;
	private Courier courier;

}
