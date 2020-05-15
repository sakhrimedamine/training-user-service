package com.sakhri.userService.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.dto.UserFilterDto;

public interface UserService extends UserDetailsService{
	
	public List<UserDto> getAllUsers();
	
	public List<UserDto> getUsersByFilter(UserFilterDto filter);
	
	public UserDto getUserDetailsByEmail(String email);
		
	public UserDto getUserByUserId(String userId);

	public UserDto createUser(UserDto user);
	
	public boolean updateUser(UserDto user, String id);
	
	public boolean deleteUser(String userId);
	
	public void deleteAllUsers();

}
