package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tegar.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>  {

}
