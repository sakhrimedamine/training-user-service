package com.sakhri.userService.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.dto.UserFilterDto;
import com.sakhri.userService.model.UserEntity;

public interface UserService extends UserDetailsService{
	
	public List<UserEntity> getAllUsers();
	
	public List<UserEntity> getUsersByFilter(UserFilterDto filter);
	
	public UserDto getUserDetailsByEmail(String email);
	
	public UserEntity getUser(Long id);
	
	public UserDto getUserByUserId(String userId);

	public UserDto createUser(UserDto user);
	
	public boolean updateUser(UserDto user, Long id);
	
	public boolean deleteUser(Long id);

}
