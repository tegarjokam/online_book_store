package com.tegar.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookCategoryRequestUpdateModel extends BookCategoryRequestCreateModel{
	
	@NotNull
	private Integer id;
}
