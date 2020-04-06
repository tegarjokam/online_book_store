package com.tegar.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tegar.model.BookModel;
import com.tegar.model.BookRequestCreateModel;
import com.tegar.model.BookRequestUpdateModel;
import com.tegar.service.BookService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/api/rest/book")
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/save")
	public BookModel save(@RequestBody @Valid BookRequestCreateModel request,
			BindingResult result,
			HttpServletResponse response) throws IOException {
		BookModel bookModel = new BookModel();
		if(result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			System.out.println("error ..........");
			return bookModel;
		} else {
			BeanUtils.copyProperties(request, bookModel);
			return bookService.saveOrUpdate(bookModel);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/update")
	public BookModel update(@RequestBody @Valid BookRequestUpdateModel request,
			BindingResult result,
			HttpServletResponse response) throws IOException {
		BookModel bookModel = new BookModel();
		if(result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			System.out.println("error ..........");
			return bookModel;
		} else {
			System.out.println(request);
			BeanUtils.copyProperties(request, bookModel);
			return bookService.saveOrUpdate(bookModel);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/deleteById/{id}")
	public BookModel delete(@PathVariable(value = "id") final Integer id) {
		return bookService.deleteById(id);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT', 'ROLE_USER')")
	@GetMapping(value = "/findById/{id}")
	public BookModel findById(@PathVariable(value = "id") final Integer id) {
		return bookService.findById(id); 
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT', 'ROLE_USER')")
	@GetMapping(value = "/findAll")
	public List<BookModel> findAll() {
		return bookService.findAll();
	}
	


}
