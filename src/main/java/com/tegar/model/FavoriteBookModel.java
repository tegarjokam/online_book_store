package com.tegar.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteBookModel extends PersistenceModel {
	
	private UserModel userModel;
	
	private List<DetailModel> details;
	
	@Getter
	@Setter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DetailModel extends PersistenceModel {
		private BookModel bookModel;
	}


}
