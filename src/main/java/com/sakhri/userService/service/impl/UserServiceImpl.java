package com.sakhri.userService.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
	public boolean updateUser(UserDto dto, String userId) {
		log.info("Updating user id {} with {}", userId, dto);
				
		final UserEntity findByUserId = userRepository.findByUserId(userId);
		
		Optional.ofNullable(findByUserId).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
		
		findByUserId.setAge(dto.getAge());
		findByUserId.setFirstName(dto.getFirstName());
		findByUserId.setLastName(dto.getLastName());
		findByUserId.setWeight(dto.getWeight());
		findByUserId.setHeight(dto.getHeight());;
		
		userRepository.save(findByUserId);
		
		return true;
	}

	@Override
	public boolean deleteUser(String userId) {
		log.info("deleting user with id {}", userId);
		
		final UserEntity findByUserId = userRepository.findByUserId(userId);
		
		Optional.ofNullable(findByUserId).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
		
		userRepository.delete(findByUserId);
		
		return true;
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Finding all users");
		
		return userRepository.findAll()
				.stream()
				.map( u -> modelMapper.map(u, UserDto.class))
				.collect(Collectors.toList());	
	}

	@Override
	public List<UserDto> getUsersByFilter(UserFilterDto filter) {
		log.info("Finding all users by filters {}", filter);
		
		String id = filter.getUserId().isEmpty() ? null : filter.getUserId();
		String firstName = filter.getFirstName().isEmpty() ? null : filter.getFirstName();
		String lastName = filter.getLastName().isEmpty() ? null : filter.getLastName();
		Short age = filter.getAge();
		Short weight = filter.getWeight();
		Short height = filter.getHeight();
		
		return userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(id,firstName,
					lastName, age, weight, height)
				.stream()
				.map( u -> modelMapper.map(u, UserDto.class))
				.collect(Collectors.toList());	
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Loading UserDetails By Username {}", username);

		UserEntity userEntity = userRepository.findByEmail(username);
		
		if(userEntity == null) throw new UsernameNotFoundException(username);	
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());

	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		log.info("Getting user by email {}", email);

		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return  modelMapper.map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		log.info("Getting user with id {}", userId);
			
		UserEntity userEntity = Optional.ofNullable(userRepository.findByUserId(userId)).orElseThrow(
				() -> new IllegalArgumentException(USER_NOT_FOUND_EXECP));
		
		return  modelMapper.map(userEntity, UserDto.class);

	}

	@Override
	public void deleteAllUsers() {
		log.info("Deleting all users");

		userRepository.deleteAll();
	
	}

}
