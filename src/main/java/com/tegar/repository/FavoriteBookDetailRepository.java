package com.tegar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tegar.entity.FavoriteBookDetail;

public interface FavoriteBookDetailRepository extends JpaRepository<FavoriteBookDetail, Integer>{
	
	@Query("FROM FavoriteBookDetail detail WHERE detail.favoriteBook.user.id = ?1 AND detail.book.id = ?2")
	List<FavoriteBookDetail> findByUserIdAndBookId(Integer userId, Integer bookId);

}
