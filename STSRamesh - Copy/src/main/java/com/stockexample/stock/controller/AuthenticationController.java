package com.stockexample.stock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockexample.stock.dto.LoginUserDto;
import com.stockexample.stock.dto.RegisterUserDto;
import com.stockexample.stock.entity.User;
import com.stockexample.stock.responses.LoginResponse;
import com.stockexample.stock.service.impl.AuthenticationServiceImpl;
import com.stockexample.stock.service.impl.JwtServiceImpl;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final JwtServiceImpl jwtService;
	private final AuthenticationServiceImpl authenticationService;

	public AuthenticationController(JwtServiceImpl jwtService, AuthenticationServiceImpl authenticationService) {
		this.jwtService = jwtService;
		this.authenticationService = authenticationService;
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
		User registeredUser = authenticationService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = authenticationService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse().setToken(jwtToken)
				.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}
}
