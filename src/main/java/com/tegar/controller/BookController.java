package com.tegar.controller;

import static com.tegar.util.EndpointConstant.PAGE;
import static com.tegar.util.EndpointConstant.PER_PAGE;
import static com.tegar.util.EndpointConstant.TITLE;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
			HttpServletResponse response) throws Exception {
		BookModel bookModel = new BookModel();
		if(result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
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
			return bookModel;
		} else {
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
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/uploadImg/{id}")
	public BookModel uploadImg(@PathVariable("id") final Integer id, @RequestParam("file") MultipartFile file) {
		return bookService.uploadImg(id, file);
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/savePlusImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BookModel savePlusImg(
			@RequestPart(value = "bookRequestModel", required = true) @Valid BookRequestCreateModel request, 
			@RequestPart(value = "file", required = true) MultipartFile file,
			BindingResult result,
			HttpServletResponse response) throws Exception {
		BookModel bookModel = new BookModel();
		if(result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return bookModel;
		} else {
			BeanUtils.copyProperties(request, bookModel);
			return bookService.saveOrUpdateWithImg(bookModel, file);
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/updatePlusImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BookModel updatePlusImg(
			@RequestPart(value = "bookRequestModel", required = true) @Valid BookRequestUpdateModel request, 
			@RequestPart(value = "file", required = true) MultipartFile file,
			BindingResult result,
			HttpServletResponse response) throws Exception {
		BookModel bookModel = new BookModel();
		if(result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return bookModel;
		} else {
			BeanUtils.copyProperties(request, bookModel);
			return bookService.saveOrUpdateWithImg(bookModel, file);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT', 'ROLE_USER')")
	@GetMapping("/lists")
	public Page<BookModel> getListBooks(
			@RequestParam(value = PAGE, required = false) Integer page,
			@RequestParam(value = PER_PAGE, required = false) Integer perPage,
			@RequestParam(value = TITLE, required = false) String title){
		return bookService.findAll(page, perPage, title);
	}
	
	

}
