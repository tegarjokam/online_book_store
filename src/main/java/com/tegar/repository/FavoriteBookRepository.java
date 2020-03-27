package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tegar.entity.FavoriteBook;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Integer> {

	FavoriteBook findByUserId(Integer userId);
	
}
