package com.tegar.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping(value = "/save")
	public CartModel saveOrUpdate(@RequestBody @Valid CartRequestModel request,
			BindingResult result,
			HttpServletResponse response) throws IOException {
		CartModel cartModel = new CartModel();
		if (result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return cartModel;
		} else {
			return cartService.saveOrUpdate(request);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping(value = "/findAll")
	public List<CartModel> findAll() {
		return cartService.findAll();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping(value = "/findById/{id}")
	public CartModel findById(@PathVariable(value = "id") final Integer id) {
		return cartService.findById(id);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping(value = "/findByUserId/{userId}")
	public CartModel findByUserId(@PathVariable(value = "userId") final Integer userId) {
		return cartService.findByUserId(userId);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@DeleteMapping(value = "/deleteByCartDetailId/{cartDetailId}")
	public CartModel deleteByCartDetailId(@PathVariable(value = "cartDetailId") final Integer cartDetailId) {
		return cartService.deleteByCartDetailId(cartDetailId);
	}
}
