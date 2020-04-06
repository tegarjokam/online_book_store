package com.tegar.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tegar.model.TransactionCreateRequestModel;
import com.tegar.model.TransactionModel;
import com.tegar.model.TransactionUpdateRequestModel;
import com.tegar.service.TransactionService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/api/rest/transaction")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping("/checkout")
	public TransactionModel checkout(@RequestBody @Valid TransactionCreateRequestModel request, 
			BindingResult result,HttpServletResponse response) throws IOException { 
		TransactionModel transactionModel = new TransactionModel(); 
		if (result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
	        return transactionModel;
	    } else
	    	return transactionService.save(request);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')") 
	@GetMapping("/findAll")
	public List<TransactionModel> findAll() {
		return transactionService.findAll(); 
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/findById/{id}")
	public TransactionModel findById(@PathVariable("id") final Integer id) {
		return transactionService.findById(id); 
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping("/findByUserId/{userId}")
	public List<TransactionModel> findByUserId(@PathVariable("userId") final Integer userId) {
		return transactionService.findByUserId(userId); 
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping("/payment")
	public TransactionModel payment(@RequestBody @Valid TransactionUpdateRequestModel request,
			BindingResult result, HttpServletResponse response) throws IOException {
		TransactionModel transactionModel = new TransactionModel();
		if (result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return transactionModel;
		} else
			return transactionService.update(request);
	}
	



}
