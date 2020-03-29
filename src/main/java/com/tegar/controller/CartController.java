package com.tegar.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tegar.model.CartModel;
import com.tegar.model.CartRequestModel;
import com.tegar.service.CartService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/api/rest/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;

	@PostMapping(name = "/save")
	private String saveOrUpdate(@RequestBody @Valid CartRequestModel request,
			BindingResult result,
			HttpServletResponse response) throws IOException {
		if (result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return "Error";
		} else {
			return cartService.saveOrUpdate(request);
		}
	}
	
	@GetMapping(name = "/findAll")
	private List<CartModel> findAll() {
		return cartService.findAll();
	}
}
