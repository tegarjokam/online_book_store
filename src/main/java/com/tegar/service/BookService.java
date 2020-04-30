package com.tegar.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.tegar.model.BookModel;

public interface BookService extends PersistenceService<BookModel, Integer> {

	BookModel uploadImg(Integer id, MultipartFile file);
	
	BookModel saveOrUpdateWithImg(BookModel bookModel, MultipartFile file)  throws IOException, Exception;
	
	Page<BookModel> findAll(Integer page, Integer perPage, String title, String isbn);

}
