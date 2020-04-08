package com.tegar.util;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<ShipmentModel> constructModel(List<Shipment> shipmentList) {
		List<ShipmentModel> shipments = new ArrayList<ShipmentModel>();
		shipmentList.forEach(data -> {
			shipments.add(constructModel(data));
		});
		return shipments;
	}

}
