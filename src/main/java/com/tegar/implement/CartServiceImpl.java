package com.tegar.implement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.tegar.entity.Book;
import com.tegar.entity.Cart;
import com.tegar.entity.CartDetail;
import com.tegar.entity.CartDetail.CartDetailStatus;
import com.tegar.entity.Persistence.Status;
import com.tegar.entity.User;
import com.tegar.model.CartModel;
import com.tegar.model.CartRequestModel;
import com.tegar.repository.BookRepository;
import com.tegar.repository.CartDetailRepository;
import com.tegar.repository.CartRepository;
import com.tegar.repository.UserRepository;
import com.tegar.service.CartService;

import static com.tegar.util.CartModelMapper.constructModel;;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartDetailRepository cartDetailRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public CartModel saveOrUpdate(CartModel entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CartModel saveOrUpdate(CartRequestModel entity) {
		User user = userRepository.findById(entity.getUserId()).orElse(null);
		//check for user
		if (user == null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User with id : " + entity.getUserId() + " not found.");
		Cart cart = cartRepository.findByUserId(entity.getUserId());
		Set<CartDetail> cartDetails = new HashSet<CartDetail>();
		//create
		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cart = cartRepository.save(cart);
			//validate book
			Book book = bookRepository.findById(entity.getBookId()).orElse(null);
			if (book == null) 
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id : " + entity.getBookId() + " not found.");
			CartDetail cartDetail = new CartDetail();
			cartDetail.setBook(book);
			cartDetail.setCart(cart);
			cartDetail.setCartDetailStatus(CartDetailStatus.CARTED);
			cartDetail = cartDetailRepository.save(cartDetail);
			cartDetails.add(cartDetail);
		} else {
			//update
			Book book = bookRepository.findById(entity.getBookId()).orElse(null);
			if (book == null) 
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id : " + entity.getBookId() + " not found.");
			List<CartDetail> currentCartDetails = cartDetailRepository.findByUserIdAndBookIdAndDetailStatus(user.getId(), book.getId(), CartDetailStatus.CARTED);
			if (currentCartDetails == null || currentCartDetails.size() == 0) {
				CartDetail cartDetail = new CartDetail();
				cartDetail.setBook(book);
				cartDetail.setCart(cart);
				cartDetail.setCartDetailStatus(CartDetailStatus.CARTED);
				cartDetail = cartDetailRepository.save(cartDetail);
				cartDetails.add(cartDetail);
			}
		}
		cart.setCartDetails(cartDetails);
		return constructModel(cart);
	}

	@Override
	public CartModel delete(CartModel entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartModel deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public CartModel findById(Integer id) {
		return constructModel(cartRepository.findById(id).orElse(null));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<CartModel> findAll() {
		return constructModel(cartRepository.findAll());
	}

	@Override
	public Long countAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public CartModel deleteByCartDetailId(Integer cartDetailId) {
		CartDetail cartDetail = cartDetailRepository.findById(cartDetailId).orElse(null);
		if (cartDetail == null) {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "cart with id " + cartDetailId + " not found.");
		}
		cartDetail.setStatus(Status.NOT_ACTIVE);
		cartDetail = cartDetailRepository.save(cartDetail);
		return constructModel(cartDetail.getCart());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public CartModel findByUserId(Integer userId) {
		return constructModel(cartRepository.findByUserId(userId));
	}

}
