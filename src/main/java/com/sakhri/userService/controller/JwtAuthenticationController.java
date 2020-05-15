package com.sakhri.userService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sakhri.userService.dto.JwtResponse;
import com.sakhri.userService.dto.LoginRequest;
import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.security.JwtTokenUtil;
import com.sakhri.userService.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class JwtAuthenticationController {

	static final String DISABLED_MSG = "USER_DISABLED";
	static final String INVALID_CREDENTIALS_MSG = "INVALID_CREDENTIALS";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {

		log.info("Authenticating request {}", authenticationRequest);

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

		UserDto user = userDetailsService.getUserDetailsByEmail(authenticationRequest.getEmail());
		
		final String token = jwtTokenUtil.generateToken(user);
		
		JwtResponse response = JwtResponse.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.age(user.getAge())
				.height(user.getHeight())
				.weight(user.getWeight())
				.userId(user.getUserId())
				.role(user.getRole())
				.token(token)
				.build();
		
		log.info("User succfully authenticated {}", response);

		return ResponseEntity.ok(response);
	}
	

	private void authenticate(String username, String password) throws Exception {
		log.info("Authenticating user with username {} and pwd {} ", username, password);
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException(DISABLED_MSG, e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(INVALID_CREDENTIALS_MSG, e);
		}
	}
}