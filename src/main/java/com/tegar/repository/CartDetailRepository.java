package com.tegar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tegar.entity.CartDetail;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer>{

	@Query(value = "FROM CartDetail detail WHERE detail.cart.user.id = ?1 AND detail.book.id = ?2")
	List<CartDetail> findByUserIdAndBookId(Integer userId, Integer bookId);
}
