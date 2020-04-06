package com.tegar.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tegar.entity.CartDetail;
import com.tegar.entity.CartDetail.CartDetailStatus;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer>{

	@Query(value = "FROM CartDetail detail WHERE detail.cart.user.id = ?1 AND detail.book.id = ?2 AND detail.cartDetailStatus = ?3")
	List<CartDetail> findByUserIdAndBookIdAndDetailStatus(Integer userId, Integer bookId, CartDetailStatus status);
	
	@Query("FROM CartDetail detail WHERE detail.id IN ?1")
	List<CartDetail> findByIds(Set<Integer> ids);
}
