package com.tegar.service;

import java.util.List;

import com.tegar.model.TransactionCreateRequestModel;
import com.tegar.model.TransactionModel;
import com.tegar.model.TransactionUpdateRequestModel;

public interface TransactionService extends PersistenceService<TransactionModel, Integer>{
	
	TransactionModel save(TransactionCreateRequestModel request);
	
	TransactionModel update(TransactionUpdateRequestModel request);
	
	List<TransactionModel> findByUserId(Integer userId);

}
