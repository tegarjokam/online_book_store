package com.tegar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tegar.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>  {
	
	Page<Book> findByTitleContainsIgnoreCase(String title, Pageable pageable);
	
	Page<Book> findAll(Pageable pageable);
	
	Page<Book> findByIsbn(String isbn, Pageable pageable);

}
