package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tegar.entity.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {

}
