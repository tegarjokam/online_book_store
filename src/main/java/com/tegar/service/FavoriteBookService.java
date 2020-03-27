package com.tegar.service;

import com.tegar.model.FavoriteBookModel;
import com.tegar.model.FavoriteBookRequestModel;

public interface FavoriteBookService extends PersistenceService<FavoriteBookModel, Integer>{

	FavoriteBookModel saveOrUpdate(FavoriteBookRequestModel request);
	FavoriteBookModel findByUserId(Integer userId);
	FavoriteBookModel deleteByFavoriteBookDetailId(Integer detailId);
}
