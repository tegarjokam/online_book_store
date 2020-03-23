package com.tegar.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String save() {
		return "Hello World!";
	}
	
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
			System.out.println(request);
			BeanUtils.copyProperties(request, bookModel);
			return bookService.saveOrUpdate(bookModel);
		}
	}
	
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
	
	@DeleteMapping(value = "/deleteById/{id}")
	public BookModel delete(@PathVariable(value = "id") final Integer id) {
		return bookService.deleteById(id);
	}
	
	@GetMapping(value = "/findById/{id}")
	public BookModel findById(@PathVariable(value = "id") final Integer id) {
		return bookService.findById(id); 
	}
	
	@GetMapping(value = "/findAll")
	public List<BookModel> findAll() {
		return bookService.findAll();
	}
	


}
