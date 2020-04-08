package com.tegar.service;

import java.util.List;

import com.tegar.model.ShipmentCreateRequestModel;
import com.tegar.model.ShipmentModel;
import com.tegar.model.ShipmentUpdateRequestModel;

public interface ShipmentService extends PersistenceService<ShipmentModel, Integer> {
	
	ShipmentModel create(ShipmentCreateRequestModel request);
	
	ShipmentModel update(ShipmentUpdateRequestModel request);
	
	List<ShipmentModel> findByUserId(Integer userId);

}
