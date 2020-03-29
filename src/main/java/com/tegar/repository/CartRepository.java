package com.tegar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tegar.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	Cart findByUserId(Integer userId);

}
