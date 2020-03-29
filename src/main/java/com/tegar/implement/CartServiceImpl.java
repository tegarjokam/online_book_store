package com.tegar.implement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
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
import com.tegar.entity.User;
import com.tegar.model.BookCategoryModel;
import com.tegar.model.BookModel;
import com.tegar.model.CartModel;
import com.tegar.model.CartModel.DetailModel;
import com.tegar.model.CartRequestModel;
import com.tegar.model.UserModel;
import com.tegar.repository.BookRepository;
import com.tegar.repository.CartDetailRepository;
import com.tegar.repository.CartRepository;
import com.tegar.repository.UserRepository;
import com.tegar.service.CartService;

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
	public String saveOrUpdate(CartRequestModel entity) {
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
			List<CartDetail> currentCartDetails = cartDetailRepository.findByUserIdAndBookId(user.getId(), book.getId());
			if (currentCartDetails == null || currentCartDetails.size() == 0) {
				CartDetail cartDetail = new CartDetail();
				cartDetail.setBook(book);
				cartDetail.setCart(cart);
				cartDetail.setCartDetailStatus(CartDetailStatus.CARTED);
				cartDetail = cartDetailRepository.save(cartDetail);
				currentCartDetails.add(cartDetail);
			}
		}
		cart.setCartDetails(cartDetails);
		return "sukses menambahkan cart ^_^";
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
	public CartModel findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CartModel> findAll() {
		List<CartModel> listCartModel = new ArrayList<CartModel>();
		cartRepository.findAll().forEach(data -> {
			CartModel cartModel = new CartModel();
			UserModel userModel = new UserModel();
			//set user model
			BeanUtils.copyProperties(data.getUser(), userModel);
			cartModel.setUserModel(userModel);
			
			//tampung list detailmodel pada cart
			List<CartModel.DetailModel> details = new ArrayList<>();
			
			//tambahkan data cartdetail pada list details
			data.getCartDetails().forEach(cartDetails -> {
				CartModel.DetailModel detail = new DetailModel();
				BookModel bookModel = new BookModel();
				BookCategoryModel bookCategoryModel = new BookCategoryModel();
				
				// copy category book model pada cartDetails
				BeanUtils.copyProperties(cartDetails.getBook().getBookCategory(), bookCategoryModel);
				bookModel.setBookCategory(bookCategoryModel);
				
				BeanUtils.copyProperties(cartDetails.getBook(), bookModel);
				detail.setBookModel(bookModel);
				
				BeanUtils.copyProperties(cartDetails, detail);
				details.add(detail);
			});
			// tambahkan cartDetails pada cartModel
			cartModel.setDetails(details);
			
			// atur semua properti yg ada pada data ke CartModel
			BeanUtils.copyProperties(data, cartModel);
			
			// tambahkan cartModel pada ListCartModel
			listCartModel.add(cartModel);
		});
		return listCartModel;
	}

	@Override
	public Long countAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CartModel deleteByCartDetailId(Integer cartDetailId) {
		return null;
	}

	@Override
	public CartModel findByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
