package com.tegar.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book")
@Where(clause = "status = 'ACTIVE'")
public class Book extends Persistence {
	private static final long serialVersionUID = 6338210243334147241L;
	
	public enum BookStatus {
		FOR_SELL, OUT_OF_STOCK, HIDE
	}
	
	@Column(length = 255)
	private String title;
	
	@Column(length = 100)
	private String isbn;
	
	@Column(length = 255)
	private String authorName;
	
	@Column(columnDefinition = "text")
	private String synopsis;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date publicationDate;
	
	@Column(columnDefinition = "double precision default '0'")
	private Double price;
	
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BookStatus bookStatus;
	
	@JoinColumn(name = "book_category_id")
	@ManyToOne(targetEntity = BookCategory.class)
	private BookCategory bookCategory;
	
	@Where(clause = "status = 'ACTIVE'")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.LAZY)
	private Set<FavoriteBookDetail> favoriteBookDetails;
	
}
