package com.tegar.util;

import org.springframework.beans.BeanUtils;

import com.tegar.entity.Shipment;
import com.tegar.model.ShipmentModel;
import com.tegar.model.UserModel;

public class ShipmentModelMapper {
	
	public static ShipmentModel constructModel(Shipment shipment) {
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(shipment.getUser(), userModel);
		
		ShipmentModel shipmentModel = new ShipmentModel();
		
		shipmentModel.setUserModel(userModel);
		shipmentModel.setTransactionModel(TransactionModelMapper.constructModel(shipment.getTransaction()));
		
		BeanUtils.copyProperties(shipment, shipmentModel);
		return shipmentModel;
	}

}
