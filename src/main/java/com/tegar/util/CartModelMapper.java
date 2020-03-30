package com.tegar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.tegar.entity.Cart;
import com.tegar.model.BookCategoryModel;
import com.tegar.model.BookModel;
import com.tegar.model.CartModel;
import com.tegar.model.CartModel.DetailModel;
import com.tegar.model.CartRequestModel;
import com.tegar.model.UserModel;


public class CartModelMapper {

	public static CartModel constructModel(Cart cart) {
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(cart.getUser(), userModel);
		
		CartModel model = new CartModel();
		model.setUserModel(userModel);

		List<CartModel.DetailModel> details = new ArrayList<>();
		cart.getCartDetails().forEach(data -> {
			CartModel.DetailModel detail = new DetailModel();
			BookModel bookModel = new BookModel();
			BookCategoryModel bookCategoryModel = new BookCategoryModel();
			
			BeanUtils.copyProperties(data.getBook().getBookCategory(), bookCategoryModel);
			bookModel.setBookCategory(bookCategoryModel);

			BeanUtils.copyProperties(data.getBook(), bookModel);
			detail.setBookModel(bookModel);
			
			BeanUtils.copyProperties(data, detail);
			details.add(detail);
		});
		model.setDetails(details);
	
		BeanUtils.copyProperties(cart, model);
		return model;
	}
	
	public static List<CartModel> constructModel(List<Cart> carts) {
		List<CartModel> models = new ArrayList<>();
		carts.forEach(cart -> {
			CartModel model = constructModel(cart);
			models.add(model);
		});
		return models;
	}
	
	public static CartModel constructModelForRequest(CartRequestModel request) {
		CartModel cartModel = new CartModel();
		CartModel.DetailModel detail = getCartDetailModel(request.getBookId());
		cartModel.setUserModel(getUserModel(request.getUserId()));
		cartModel.setDetails(Arrays.asList(detail));
		return cartModel;
	}
	
	private static UserModel getUserModel(Integer userId) {
		UserModel userModel = new UserModel();
		userModel.setId(userId);
		return userModel;
	}
	
	private static CartModel.DetailModel getCartDetailModel(Integer bookId) {
		CartModel.DetailModel detail = new CartModel.DetailModel();
		BookModel bookModel = new BookModel();
		bookModel.setId(bookId);
		detail.setBookModel(bookModel);
		return detail;
	}
}