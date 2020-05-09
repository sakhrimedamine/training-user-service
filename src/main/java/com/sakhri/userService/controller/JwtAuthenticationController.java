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

import com.sakhri.userService.beans.LoginRequest;
import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.model.JwtResponse;
import com.sakhri.userService.security.JwtTokenUtil;
import com.sakhri.userService.service.UserService;

@RestController
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

//		final UserDetails userDetails = userDetailsService
//				.loadUserByUsername(authenticationRequest.getEmail());
//
//		final String token = jwtTokenUtil.generateToken(userDetails);

		UserDto user = userDetailsService.getUserDetailsByEmail(authenticationRequest.getEmail());
		
		final String token = jwtTokenUtil.generateToken(user);
		
		JwtResponse response = JwtResponse.builder().firstName(user.getFirstName())
				.lastName(user.getLastName())
				.age(user.getAge())
				.userId(user.getUserId())
				.role(user.getRole())
				.token(token)
				.build();
		
		return ResponseEntity.ok(response);
	}
	

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}