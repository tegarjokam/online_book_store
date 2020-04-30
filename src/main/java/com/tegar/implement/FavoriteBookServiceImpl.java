package com.tegar.implement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.tegar.entity.Book;
import com.tegar.entity.FavoriteBook;
import com.tegar.entity.FavoriteBookDetail;
import com.tegar.entity.Persistence.Status;
import com.tegar.entity.User;
import com.tegar.model.FavoriteBookModel;
import com.tegar.model.FavoriteBookRequestModel;
import com.tegar.repository.BookRepository;
import com.tegar.repository.FavoriteBookDetailRepository;
import com.tegar.repository.FavoriteBookRepository;
import com.tegar.repository.UserRepository;
import com.tegar.service.FavoriteBookService;
import static com.tegar.util.FavoriteBookModelMapper.constructModel;

@Service
public class FavoriteBookServiceImpl implements FavoriteBookService {
	
	@Autowired
	private FavoriteBookRepository favoriteBookRepository;
	
	@Autowired
	private FavoriteBookDetailRepository favoriteBookDetailRepository;
	 
	@Autowired
	private BookRepository bookRepository;
	 
	@Autowired
	private UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(FavoriteBookServiceImpl.class);

	@Override
	public FavoriteBookModel saveOrUpdate(FavoriteBookModel entity) {
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public FavoriteBookModel saveOrUpdate(FavoriteBookRequestModel request) {
		// validate user
		User user = userRepository.findById(request.getUserId()).orElse(null);
		
		if (user == null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "User with id: " + request.getUserId() + " not found");
		FavoriteBook favoriteBook = favoriteBookRepository.findByUserId(user.getId());
		Set<FavoriteBookDetail> favoriteBookDetails = new HashSet<>(); // initialize
		if (favoriteBook == null) {
			favoriteBook = new FavoriteBook(); 
			favoriteBook.setUser(user);
			favoriteBook = favoriteBookRepository.save(favoriteBook);
			// validate book
			Book book = bookRepository.findById(request.getBookId()).orElse(null);
			if (book == null)
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id: " + request.getBookId() + " not found");
			// detail 
			favoriteBookDetails.add(saveFavoriteBookDetail(favoriteBook, book));
		} else {
			// update
			Book book = bookRepository.findById(request.getBookId()).orElse(null);
			if (book == null)
				throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Book with id: " + request.getBookId() + " not found");
			List<FavoriteBookDetail> currentFavoriteBookDetails = favoriteBookDetailRepository.findByUserIdAndBookId(user.getId(), book.getId());
			if (currentFavoriteBookDetails == null || currentFavoriteBookDetails.size() == 0) {
				favoriteBookDetails.add(saveFavoriteBookDetail(favoriteBook, book));
			}
		}
		favoriteBook.setFavoriteBookDetails(favoriteBookDetails);
		return constructModel(favoriteBook);
	}
	
	
	
	private FavoriteBookDetail saveFavoriteBookDetail(FavoriteBook favoriteBook, Book book) {
		FavoriteBookDetail favoriteBookDetail = new FavoriteBookDetail();
		favoriteBookDetail.setBook(book);
		favoriteBookDetail.setFavoriteBook(favoriteBook);
		return favoriteBookDetailRepository.save(favoriteBookDetail);
	}

	@Override
	public FavoriteBookModel delete(FavoriteBookModel entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FavoriteBookModel deleteById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public FavoriteBookModel deleteByFavoriteBookDetailId(Integer detailId) {
		FavoriteBookDetail favoriteBookDetail = favoriteBookDetailRepository.findById(detailId).orElse(null);
		if (favoriteBookDetail == null)
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Favorite Book Detaild with id: " + detailId + " not found.");
		favoriteBookDetail.setStatus(Status.NOT_ACTIVE);
		favoriteBookDetail = favoriteBookDetailRepository.save(favoriteBookDetail);
		return constructModel(favoriteBookDetail.getFavoriteBook());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public FavoriteBookModel findById(Integer id) {
		return constructModel(favoriteBookRepository.findById(id).orElse(null));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<FavoriteBookModel> findAll() {
		return constructModel(favoriteBookRepository.findAll());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Long countAll() {
		return favoriteBookRepository.count();
	}


	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public FavoriteBookModel findByUserId(Integer userId) {
		return constructModel(favoriteBookRepository.findByUserId(userId));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public FavoriteBookModel findByUserUsername(String username) {
		logger.info(constructModel(favoriteBookRepository.findByUserEmail(username)).toString());
		return constructModel(favoriteBookRepository.findByUserEmail(username));
	}


}
