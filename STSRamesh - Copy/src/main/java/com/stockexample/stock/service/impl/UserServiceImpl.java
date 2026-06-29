package com.stockexample.stock.service.impl;

import org.springframework.stereotype.Service;

import com.stockexample.stock.entity.User;
import com.stockexample.stock.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl {
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> allUsers() {
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);

		return users;
	}
}
