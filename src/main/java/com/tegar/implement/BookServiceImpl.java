package com.tegar.implement;

import static com.tegar.util.PageRequestUtil.constructPageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.tegar.entity.Book;
import com.tegar.entity.BookCategory;
import com.tegar.entity.Persistence.Status;
import com.tegar.exception.BookServiceException;
import com.tegar.model.BookCategoryModel;
import com.tegar.model.BookModel;
import com.tegar.repository.BookCategoryRepository;
import com.tegar.repository.BookRepository;
import com.tegar.service.BookService;
import com.tegar.service.MinioService;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BookCategoryRepository bookCategoryRepository;
	
	@Autowired
	private MinioService minioService;

	Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
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

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BookModel uploadImg(Integer id, MultipartFile file) {
		BookModel entity = new BookModel();
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id: " + id + " not found");
		
		// upload image
		try {
			String imageUrl = minioService.uploadImg(UUID.randomUUID().toString(),
					file.getInputStream(), file.getContentType());
			book.setImageUrl(imageUrl);
			book = bookRepository.save(book);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Problem upload file");
		}
		BeanUtils.copyProperties(book, entity);
		return entity;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public BookModel saveOrUpdateWithImg(BookModel entity, MultipartFile file) throws IOException, Exception {
		Book book = new Book();
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
			String imageUrl = minioService.uploadImg(UUID.randomUUID().toString(),file.getInputStream(), file.getContentType());
			book.setImageUrl(imageUrl);
			book = bookRepository.save(book);
		} else
			try {
					bookCategory = bookCategoryRepository.findById(entity.getBookCategoryId()).orElse(null);
					if (bookCategory == null) 
						throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book Category with id: " + entity.getId() + " not found.");
					book = new Book();
					book.setBookCategory(bookCategory);
					String imageUrl = minioService.uploadImg(UUID.randomUUID().toString(),file.getInputStream(), file.getContentType());
					BeanUtils.copyProperties(entity, book);
					book.setImageUrl(imageUrl);
					book = bookRepository.save(book);
			} catch (Exception e) {
				e.printStackTrace();
			}
		BookCategoryModel bookCategoryModel = new BookCategoryModel();
		BeanUtils.copyProperties(book.getBookCategory(), bookCategoryModel);
		BeanUtils.copyProperties(book, entity);
		entity.setBookCategoryId(bookCategoryModel.getId());
		entity.setBookCategory(bookCategoryModel);
		return entity;
	}

	@Override
	public Page<BookModel> findAll(Integer page, Integer perPage, String title, String isbn) {
		
		if (StringUtils.isNotBlank(title)) {
			Page<BookModel> pageBook = bookRepository.findByTitleContainsIgnoreCase(title, constructPageRequest(page, perPage)).map(data -> {
				BookModel entity = new BookModel();
				BeanUtils.copyProperties(data, entity);
	
				BookCategoryModel bookCategoryModel = new BookCategoryModel();
				BeanUtils.copyProperties(data.getBookCategory(), bookCategoryModel);
				entity.setBookCategoryId(data.getBookCategory().getId());
				entity.setBookCategory(bookCategoryModel);
				return entity;
			});
			logger.info(Integer.toString(pageBook.getContent().size()));
			if (pageBook.getContent().size() == 0)
				throw new BookServiceException(404, "Title tidak ditemukan");
			return pageBook;
		} else if (StringUtils.isNotBlank(isbn)){
			Page<BookModel> pageBook = bookRepository.findByIsbn(isbn, constructPageRequest(page, perPage)).map(data -> {
				BookModel entity = new BookModel();
				BeanUtils.copyProperties(data, entity);
	
				BookCategoryModel bookCategoryModel = new BookCategoryModel();
				BeanUtils.copyProperties(data.getBookCategory(), bookCategoryModel);
				entity.setBookCategoryId(data.getBookCategory().getId());
				entity.setBookCategory(bookCategoryModel);
				return entity;
			});
			if (pageBook.getContent().size() == 0)
				throw new BookServiceException(404, "isbn tidak ditemukan");
			return pageBook;
		} else {
			return bookRepository.findAll(constructPageRequest(page, perPage)).map(data -> {
				BookModel entity = new BookModel();
				BeanUtils.copyProperties(data, entity);
	
				BookCategoryModel bookCategoryModel = new BookCategoryModel();
				BeanUtils.copyProperties(data.getBookCategory(), bookCategoryModel);
				entity.setBookCategoryId(data.getBookCategory().getId());
				entity.setBookCategory(bookCategoryModel);
				return entity;
			});
		}
	}

}
