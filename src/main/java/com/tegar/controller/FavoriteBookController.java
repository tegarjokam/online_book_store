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

import com.tegar.model.FavoriteBookModel;
import com.tegar.model.FavoriteBookRequestModel;
import com.tegar.service.FavoriteBookService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/api/rest/favorite-book")
public class FavoriteBookController {
	
	@Autowired
	private FavoriteBookService favoriteBookService;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping(value = "/save")
	public FavoriteBookModel saveOrUpdate(@RequestBody @Valid FavoriteBookRequestModel request,
			BindingResult result,
			HttpServletResponse response) throws IOException {
		FavoriteBookModel favoritBookModel = new FavoriteBookModel();
		if (result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return favoritBookModel;
		} else {
			return favoriteBookService.saveOrUpdate(request);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@DeleteMapping(value = "/deleteByFavoriteBookDetailId/{detailId}")
	public FavoriteBookModel deleteById(@PathVariable(value = "detailId") final Integer detailId) {
		return favoriteBookService.deleteByFavoriteBookDetailId(detailId);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping(value = "/findAll")
	public List<FavoriteBookModel> findAll() {
		return favoriteBookService.findAll();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping(value = "/findById/{id}")
	public FavoriteBookModel findById(@PathVariable(value = "id") final Integer id) {
		return favoriteBookService.findById(id);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@GetMapping(value = "/findByUserId/{userId}")
	public FavoriteBookModel findByUserId(@PathVariable(value = "userId") final Integer userId) {
		return favoriteBookService.findByUserId(userId);
	}

}
