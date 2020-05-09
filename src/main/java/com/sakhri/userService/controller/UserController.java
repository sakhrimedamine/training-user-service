package com.sakhri.userService.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.context.request.WebRequest;

import com.sakhri.userService.beans.CreateUserResponse;
import com.sakhri.userService.beans.CreateUserResquest;
import com.sakhri.userService.beans.UpdateUserResquest;
import com.sakhri.userService.dto.ApiResponseDto;
import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.dto.UserFilterDto;
import com.sakhri.userService.model.UserEntity;
import com.sakhri.userService.service.UserService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController()
@RequestMapping("users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody(required = true) CreateUserResquest user,
			WebRequest request) {
		
		UserDto dto = modelMapper.map(user, UserDto.class);
		UserDto createdUser = userService.createUser(dto);
		CreateUserResponse response = modelMapper.map(createdUser, CreateUserResponse.class);

		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDto> updateUser(@PathVariable("id") Long id, 
			@Valid @RequestBody(required = true) UpdateUserResquest user, WebRequest request) {
				
		UserDto dto = modelMapper.map(user, UserDto.class);
		userService.updateUser(dto, id);
		return ResponseEntity.ok(ApiResponseDto.builder()
									.message("User successfully updated")
									.timestamp(LocalDateTime.now())
									.details(request.getDescription(false))
									.build());
									
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto> deleteUser(@PathVariable("id") Long id, WebRequest request) {
		
		userService.deleteUser(id);
		return ResponseEntity.ok(ApiResponseDto.builder()
									.message("User successfully deleted")
									.timestamp(LocalDateTime.now())
									.details(request.getDescription(false))
									.build());
	}
	
	@GetMapping
	public ResponseEntity<List<UserEntity>> getUsers() {
		log.info("Getting all users");
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@GetMapping(params = {"firstName","lastName", "userId", "weight", "height", "age"})
	public ResponseEntity<List<UserEntity>> getUsersByFilter(UserFilterDto dto) {
		log.info("Getting users by filters {}", dto);
		return ResponseEntity.ok(userService.getUsersByFilter(dto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable("id") String id) {
		return ResponseEntity.ok(modelMapper.map(userService.getUserByUserId(id), UserDto.class));
	}
}
