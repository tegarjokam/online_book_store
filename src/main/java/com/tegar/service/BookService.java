package com.tegar.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.tegar.model.BookModel;

public interface BookService extends PersistenceService<BookModel, Integer> {

	BookModel uploadImg(Integer id, MultipartFile file);
	
	BookModel saveOrUpdateWithImg(BookModel bookModel, MultipartFile file)  throws IOException, Exception;

}
