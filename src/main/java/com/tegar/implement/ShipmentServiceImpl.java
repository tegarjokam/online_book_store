package com.tegar.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.tegar.entity.Persistence.Status;
import com.tegar.entity.Shipment;
import com.tegar.entity.Shipment.ShipmentStatus;
import com.tegar.entity.Transaction;
import com.tegar.entity.Transaction.TransactionStatus;
import com.tegar.entity.User;
import com.tegar.model.ShipmentCreateRequestModel;
import com.tegar.model.ShipmentModel;
import com.tegar.model.ShipmentUpdateRequestModel;
import com.tegar.repository.ShipmentRepository;
import com.tegar.repository.TransactionRepository;
import com.tegar.repository.UserRepository;
import com.tegar.service.ShipmentService;

import lombok.extern.slf4j.Slf4j;

import static com.tegar.util.ShipmentModelMapper.constructModel;

@Service
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	

	@Override
	public ShipmentModel saveOrUpdate(ShipmentModel entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShipmentModel delete(ShipmentModel entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShipmentModel deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShipmentModel findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipmentModel> findAll() {
		return null;
	}

	@Override
	public Long countAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ShipmentModel create(ShipmentCreateRequestModel request) {
		
		User user = userRepository.findById(request.getUserId()).orElse(null);
		if (user == null) 
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User with id : " + request.getUserId() + "not found.");
		
		Transaction transaction = transactionRepository.findById(request.getTransactionId()).orElse(null);
		if (transaction == null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Transaction with id :" + request.getTransactionId() + "not found.");
		
		if (!transaction.getTransactionStatus().equals(TransactionStatus.SETTLED))
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Status with id :" + request.getTransactionId() + "is not SETTLED");

		Shipment shipment =  new Shipment();
		shipment.setAddress(user.getAddress());
		shipment.setCourier(request.getCourier());
		shipment.setShipmentStatus(ShipmentStatus.ORDERED);
		shipment.setTransaction(transaction);
		shipment.setStatus(Status.ACTIVE);
		shipment.setUser(user);
		
		shipment = shipmentRepository.save(shipment);
		
		log.info("SHIPMENT REQUEST ====> " + request.toString());
		log.info("SHIPMENT SAVED ====> " + shipment.toString());
		log.info("SHIPMENTMODEL ===> " + constructModel(shipment).toString());
		return constructModel(shipment);
	}

	@Override
	public ShipmentModel update(ShipmentUpdateRequestModel request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipmentModel> findByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
