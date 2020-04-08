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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tegar.model.ShipmentCreateRequestModel;
import com.tegar.model.ShipmentModel;
import com.tegar.service.ShipmentService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping("/api/rest/shipment")
public class ShipmentController {
	
	@Autowired
	private ShipmentService shipmentService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/create")
	public ShipmentModel create(@RequestBody @Valid ShipmentCreateRequestModel request,
			BindingResult result,
			HttpServletResponse response) throws IOException {
		ShipmentModel shipmentModel = new ShipmentModel();
		if (result.hasErrors()) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), result.getAllErrors().toString());
			return shipmentModel;
		} else {
			return shipmentService.create(request);
		}
	}
}
