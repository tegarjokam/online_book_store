package com.tegar.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tegar.entity.Book;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookModel extends PersistenceModel {
	
	private String title;
	private String isbn;
	private String authorName;
	private String synopsis;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
	private Date publicationDate;
	
	private Double price;

	private Book.BookStatus bookStatus;
	
	private Integer bookCategoryId;
	
	private BookCategoryModel bookCategory;

}
