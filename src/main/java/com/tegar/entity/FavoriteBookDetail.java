package com.tegar.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite_book_detail")
@Where(clause = "status = 'ACTIVE'")
public class FavoriteBookDetail extends Persistence{

	private static final long serialVersionUID = 1130010943968579177L;
	
	@JoinColumn(name = "book_id")
	@ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY) 
	private Book book;
	
	@JoinColumn(name = "favorite_book_id")
	@ManyToOne(targetEntity = FavoriteBook.class, fetch = FetchType.LAZY)
	private FavoriteBook favoriteBook;

}
