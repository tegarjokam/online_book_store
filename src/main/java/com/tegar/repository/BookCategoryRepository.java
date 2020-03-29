package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tegar.entity.BookCategory;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {

}
