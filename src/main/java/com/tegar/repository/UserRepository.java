package com.tegar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tegar.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findById(Integer userId);

}
