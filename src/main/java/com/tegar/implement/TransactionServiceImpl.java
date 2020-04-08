package com.tegar.implement;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.tegar.entity.CartDetail;
import com.tegar.entity.Persistence.Status;
import com.tegar.entity.Transaction;
import com.tegar.entity.Transaction.PaymentMethod;
import com.tegar.entity.Transaction.TransactionStatus;
import com.tegar.entity.TransactionDetail;
import com.tegar.entity.User;
import com.tegar.model.TransactionCreateRequestModel;
import com.tegar.model.TransactionModel;
import com.tegar.model.TransactionUpdateRequestModel;
import com.tegar.repository.CartDetailRepository;
import com.tegar.repository.TransactionDetailRepository;
import com.tegar.repository.TransactionRepository;
import com.tegar.repository.UserRepository;
import com.tegar.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

import static com.tegar.util.TransactionModelMapper.constructModel;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartDetailRepository cartDetailRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private TransactionDetailRepository transactionDetailRepository;

	@Override
	public TransactionModel saveOrUpdate(TransactionModel entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionModel delete(TransactionModel entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionModel deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public TransactionModel findById(Integer id) {
		// TODO Auto-generated method stub
		return constructModel(transactionRepository.findById(id).orElse(null));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<TransactionModel> findAll() {
		return constructModel(transactionRepository.findAll());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Long countAll() {
		return transactionRepository.count();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TransactionModel save(TransactionCreateRequestModel request) {
		
		//validate user
		User user = userRepository.findById(request.getUserId()).orElse(null);
		
		if (user == null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User with id: " + request.getUserId() + " not found");

		List<CartDetail> cartDetails = cartDetailRepository.findByIds(request.getCartDetailIds());
		Set<TransactionDetail> transactionDetails = new HashSet<>(); 
		
		if (cartDetails == null || cartDetails.size() == 0)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Cart details not found");
		
		Transaction transaction = new Transaction();
		transaction.setInvoiceNumber(UUID.randomUUID().toString());
		transaction.setPaymentMethod(PaymentMethod.BANK_TRANSFER); // if many options, it should be from request
		transaction.setStatus(Status.ACTIVE);
		transaction.setTransactionStatus(TransactionStatus.PENDING); transaction.setUser(user);
		transaction = transactionRepository.save(transaction);
		
		// detail
		for (CartDetail data : cartDetails) {
			TransactionDetail transactionDetail = new TransactionDetail();
			transactionDetail.setBook(data.getBook());
			transactionDetail.setCartDetail(data);
			transactionDetail.setPrice(data.getBook().getPrice());
			transactionDetail.setTransaction(transaction);
			transactionDetail = transactionDetailRepository.save(transactionDetail);
			transactionDetails.add(transactionDetail);
			// update cart detail data.setCartDetailStatus(CartDetailStatus.TRANSACTED); cartDetailRepository.save(data);
		} 
		transaction.setTransactionDetails(transactionDetails);
		log.info("TRANSACTION DETAILS ==== "+ transactionDetails.toString());
		log.info(" TRANSACTION  === "+ transaction.toString());
		return constructModel(transaction);
	}

	@Override
	public TransactionModel update(TransactionUpdateRequestModel request) {
		Transaction transaction = transactionRepository.findById(request.getTransactionId()).orElse(null);
		if (transaction == null) 
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Transaction with id : " + request.getTransactionId() + "not found.");
		
		transaction.setReceiptImageUrl(request.getReceiptImageUrl());
		transaction.setTransactionStatus(TransactionStatus.SETTLED);
		transaction.setPaymentTime(new Date());
		transaction = transactionRepository.save(transaction);
		
		log.info("TRANSACTION ===>>   " + transaction.toString());
		return constructModel(transaction);
	}

	@Override
	public List<TransactionModel> findByUserId(Integer userId) {
		return constructModel(transactionRepository.findByUserId(userId));
	}

}
