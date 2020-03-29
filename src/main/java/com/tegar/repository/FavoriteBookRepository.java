package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tegar.entity.FavoriteBook;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Integer> {

	FavoriteBook findByUserId(Integer userId);
	
}
