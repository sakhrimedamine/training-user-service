package com.sakhri.userService.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sakhri.userService.dto.CreateUserResponse;
import com.sakhri.userService.dto.CreateUserResquest;
import com.sakhri.userService.dto.UpdateUserResquest;
import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.dto.UserFilterDto;
import com.sakhri.userService.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController()
@RequestMapping(
		value = "users",
		consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
		produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping()
	public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody(required = true) CreateUserResquest user) {
		log.info("Creating new user with data {}", user);
		
		UserDto dto = modelMapper.map(user, UserDto.class);
		
		UserDto createdUser = userService.createUser(dto);
		
		CreateUserResponse response = modelMapper.map(createdUser, CreateUserResponse.class);

		log.info("User succesfully created  {}", response);

		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(response);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<Boolean> updateUser(@PathVariable("userId") String userId, 
			@Valid @RequestBody(required = true) UpdateUserResquest user) {
		
		log.info("Updating existing user with id {} and data {}", userId, user);

		UserDto dto = modelMapper.map(user, UserDto.class);
		
		final boolean updateUser = userService.updateUser(dto, userId);
		
		log.info("User succesfully updated.");

		return ResponseEntity.ok(updateUser);
									
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable("userId") String userId) {
		
		log.info("Deleting existing user with userId {}", userId);

		final boolean deleteUser = userService.deleteUser(userId);
		
		log.info("User succesfully deleted.");

		return ResponseEntity.ok(deleteUser);
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> getUsers() {
		log.info("Getting all users");
		
		final List<UserDto> allUsers = userService.getAllUsers();
		
		log.info("Users retreived {}", allUsers);

		return ResponseEntity.ok(allUsers);
	}
	
	@GetMapping(params = {"firstName","lastName", "userId", "weight", "height", "age"})
	public ResponseEntity<List<UserDto>> getUsersByFilter(UserFilterDto dto) {
		log.info("Getting users by filters {}", dto);
		
		final List<UserDto> usersByFilter = userService.getUsersByFilter(dto);
		
		log.info("Filtred users retreived {}", usersByFilter);

		return ResponseEntity.ok(usersByFilter);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
		log.info("Getting users by userId {}", userId);

		final UserDto userByUserId = userService.getUserByUserId(userId);
		
		final UserDto dto = modelMapper.map(userByUserId, UserDto.class);
		
		log.info("User retreived {}", dto);

		return ResponseEntity.ok(dto);
	}
}
