package com.sakhri.userService.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.dto.UserFilterDto;
import com.sakhri.userService.model.UserEntity;
import com.sakhri.userService.repository.UserRepository;
import com.sakhri.userService.service.UserService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Service
public class UserServiceImpl implements UserService {
	
	private static final String USER_NOT_FOUND_EXECP = "User not found.";
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto dto) {
		log.info("Creating user {}", dto);
		dto.setUserId(UUID.randomUUID().toString());
		dto.setEncryptedPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
		UserEntity user = modelMapper.map(dto, UserEntity.class);
		log.info("Saving to DB Entity {}", user);
		userRepository.save(user);
		UserDto returnValue = modelMapper.map(user, UserDto.class);
		return returnValue;
	}

	@Override
	public boolean updateUser(UserDto dto, Long id) {
		log.info("Updating user id {} with {}", id, dto);
		UserEntity user = modelMapper.map(dto, UserEntity.class);
		userRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
		userRepository.save(user);
		return true;
	}

	@Override
	public boolean deleteUser(Long id) {
		log.info("deleting user with id {}", id);
		UserEntity user = userRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
		userRepository.delete(user);
		return true;
	}

	@Override
	public UserEntity getUser(Long id) {
		log.info("Getting user with if {}", id);
		return userRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
	}

	@Override
	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<UserEntity> getUsersByFilter(UserFilterDto filter) {
		String id = filter.getUserId().isEmpty() ? null : filter.getUserId();
		String firstName = filter.getFirstName().isEmpty() ? null : filter.getFirstName();
		String lastName = filter.getLastName().isEmpty() ? null : filter.getLastName();
		Short age = filter.getAge().isEmpty() ? null : Short.valueOf(filter.getAge());
		Short weight = filter.getWeight().isEmpty() ? null : Short.valueOf(filter.getWeight());
		Short height = filter.getHeight().isEmpty() ? null : Short.valueOf(filter.getHeight());

		return userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(id,firstName, lastName, age, weight, height);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(username);
		
		if(userEntity == null) throw new UsernameNotFoundException(username);	
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());

	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return  modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		log.info("Getting user with if {}", userId);
			
		UserEntity userEntity = Optional.ofNullable(userRepository.findByUserId(userId)).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
		
		return  modelMapper.map(userEntity, UserDto.class);

	}

}
