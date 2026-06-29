package com.stockexample.stock.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stockexample.stock.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByEmail(String email);
}