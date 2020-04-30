package com.tegar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.tegar.entity.FavoriteBook;
import com.tegar.model.BookCategoryModel;
import com.tegar.model.BookModel;
import com.tegar.model.FavoriteBookModel;
import com.tegar.model.FavoriteBookModel.DetailModel;
import com.tegar.model.FavoriteBookRequestModel;
import com.tegar.model.UserModel;

public class FavoriteBookModelMapper {

	public static FavoriteBookModel constructModel(FavoriteBook favoriteBook) {
			UserModel userModel = new UserModel(); 
			Logger logger = LoggerFactory.getLogger(FavoriteBookModelMapper.class);
			logger.info(favoriteBook.getUser().toString());
			BeanUtils.copyProperties(favoriteBook.getUser(), userModel);
			FavoriteBookModel model = new FavoriteBookModel();
			// set userModel kedalam model
			model.setUserModel(userModel);
			List<FavoriteBookModel.DetailModel> details = new ArrayList<>(); 
			
			favoriteBook.getFavoriteBookDetails().forEach(data -> {
				FavoriteBookModel.DetailModel detail = new DetailModel(); 
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
			BeanUtils.copyProperties(favoriteBook, model);
			return model;
	}
	
	public static List<FavoriteBookModel> constructModel(List<FavoriteBook> favoriteBooks) {
		List<FavoriteBookModel> models = new ArrayList<>(); 
		favoriteBooks.forEach(favoriteBook -> {
			FavoriteBookModel model = constructModel(favoriteBook);
			models.add(model); 
		});
		return models;
	}
	
	public static FavoriteBookModel construcModelForRequest(FavoriteBookRequestModel request) {
		FavoriteBookModel favoriteBookModel = new FavoriteBookModel();
		FavoriteBookModel.DetailModel detail = getFavoriteDetailModel(request.getBookId());
		favoriteBookModel.setUserModel(getUserModel(request.getUserId())); 
		favoriteBookModel.setDetails(Arrays.asList(detail));
		return favoriteBookModel;
	}
	   
	private static UserModel getUserModel(Integer userId) { 
		UserModel userModel = new UserModel(); 
		userModel.setId(userId);
		return userModel;
	}
	
	private static FavoriteBookModel.DetailModel getFavoriteDetailModel(Integer bookId) {
		FavoriteBookModel.DetailModel detail = new FavoriteBookModel.DetailModel();
		BookModel bookModel = new BookModel(); 
		bookModel.setId(bookId); 
		detail.setBookModel(bookModel); 
		return detail;
	}
			
}
