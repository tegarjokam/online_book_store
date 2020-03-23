package com.tegar.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.tegar.entity.Book;
import com.tegar.entity.BookCategory;
import com.tegar.entity.Persistence.Status;
import com.tegar.model.BookCategoryModel;
import com.tegar.model.BookModel;
import com.tegar.repository.BookCategoryRepository;
import com.tegar.repository.BookRepository;
import com.tegar.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	BookCategoryRepository bookCategoryRepository;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BookModel saveOrUpdate(BookModel entity) {
		Book book;
		BookCategory bookCategory;
		if (entity.getId() != null) {
			book = bookRepository.findById(entity.getId()).orElse(null);
			if (book == null) 
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id:" + entity.getId() + " not found.");
			if (!book.getBookCategory().getId().equals(entity.getBookCategoryId())) {
				bookCategory = bookCategoryRepository.findById(entity.getBookCategoryId()).orElse(null);
				book.setBookCategory(bookCategory);
			}
			BeanUtils.copyProperties(entity, book);
			book = bookRepository.save(book);
		} else {
			bookCategory = bookCategoryRepository.findById(entity.getBookCategoryId()).orElse(null);
			if (bookCategory == null) 
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book Category with id: " + entity.getId() + " not found.");
			book = new Book();
			book.setBookCategory(bookCategory);
			BeanUtils.copyProperties(entity, book);
			book = bookRepository.save(book);
		}
		BookCategoryModel bookCategoryModel = new BookCategoryModel();
		BeanUtils.copyProperties(book.getBookCategory(), bookCategoryModel);
		BeanUtils.copyProperties(book, entity);
		entity.setBookCategoryId(bookCategoryModel.getId());
		entity.setBookCategory(bookCategoryModel);
		System.out.println(entity.toString());
		return entity;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BookModel delete(BookModel entity) {
		if (entity.getId() != null) {
			Book book = bookRepository.findById(entity.getId()).orElse(null);
			if (book == null)
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id: " + entity.getId());
			book.setStatus(Status.NOT_ACTIVE);
			book = bookRepository.save(book);
			BeanUtils.copyProperties(book, entity);
			return entity;
		} else {
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Id cannot be null");
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BookModel deleteById(Integer id) {
		if (id != null) {
			BookModel entity = new BookModel();
			Book book = bookRepository.findById(id).orElse(null);
			if (book == null)
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id: " + id + " not found");
			
			book.setStatus(Status.NOT_ACTIVE);
			book = bookRepository.save(book);

			BeanUtils.copyProperties(book, entity);
			return entity;
		} else
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Id cannot be null");
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public BookModel findById(Integer id) {
		if (id != null) {
			BookModel entity = new BookModel();
			Book book = bookRepository.findById(id).orElse(null);
			if (book == null)
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id: " + id + " not found");
			
			BookCategoryModel bookCategoryModel = new BookCategoryModel();
			BeanUtils.copyProperties(book.getBookCategory(), bookCategoryModel);
			entity.setBookCategoryId(book.getBookCategory().getId());
			entity.setBookCategory(bookCategoryModel);
			
			BeanUtils.copyProperties(book, entity);
			return entity;
		} else
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Id cannot be null");
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<BookModel> findAll() {
		List<BookModel> entities = new ArrayList<>();
		bookRepository.findAll().forEach(data -> {
			BookModel entity = new BookModel();
			BeanUtils.copyProperties(data, entity);

			BookCategoryModel bookCategoryModel = new BookCategoryModel();
			BeanUtils.copyProperties(data.getBookCategory(), bookCategoryModel);
			entity.setBookCategoryId(data.getBookCategory().getId());
			entity.setBookCategory(bookCategoryModel);
			
			entities.add(entity);
		});
		return entities;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Long countAll() {
		return bookRepository.count();
	}



}
