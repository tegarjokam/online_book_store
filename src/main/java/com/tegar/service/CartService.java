package com.tegar.service;

import com.tegar.model.CartModel;
import com.tegar.model.CartRequestModel;

public interface CartService extends PersistenceService<CartModel, Integer>{
	
	String saveOrUpdate(CartRequestModel entity);
	
	CartModel deleteByCartDetailId(Integer cartDetailId);
	
	CartModel findByUserId(Integer userId);

}
