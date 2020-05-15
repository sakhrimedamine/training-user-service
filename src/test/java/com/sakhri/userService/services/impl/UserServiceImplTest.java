package com.sakhri.userService.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sakhri.userService.dto.UserDto;
import com.sakhri.userService.model.UserEntity;
import com.sakhri.userService.repository.UserRepository;
import com.sakhri.userService.service.impl.UserServiceImpl;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserServiceImplTest {

	private static final String USER_NOT_FOUND_EXECP = "User not found.";
	
	@Spy
	private ModelMapper modelMapper;
	
	@Spy
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userServiceImpl;


	@BeforeEach
	public void configModelMapper() {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}
	
	@Test
	void whenCreateUserWithValidDTOThenCreatIt() {
		//Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		UserDto dto = UserDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.userId(UUID.randomUUID().toString())
				.email("user1@gmail.com")
				.password("abcdefghijklmn")
				.height(height)
				.weight(weight)
				.age(age)
				.build();		
		// When
		UserDto createdUser = userServiceImpl.createUser(dto);
		
		//then
		assertEquals(firstName, createdUser.getFirstName());
		assertEquals(lastName, createdUser.getLastName());
		assertEquals(age, createdUser.getAge());
		assertEquals(height, createdUser.getHeight());
		assertEquals(weight, createdUser.getWeight());
	}

	@Test
	void whenUpdateserWithExistingIdThenReturnTrue() {
		//Given
		final String userId = UUID.randomUUID().toString();
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final short age = 22;
		final short height = 180;
		final short weight = 88;
		
		UserDto dto = UserDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.userId(userId)
				.email("user1@gmail.com")
				.password("abcdefghijklmn")
				.height(height)
				.weight(weight)
				.age(age)
				.build();
		
		when(userRepository.findByUserId(userId)).thenReturn(new UserEntity());

		// When
		boolean updateUser = userServiceImpl.updateUser(dto, userId);
		
		//then
		assertEquals(updateUser, true);
	}
	
	@Test()
	void whenUpdateserWithNotFoundUserThenTthowIllegalArgumentException() {
		//Given
		final String userId = UUID.randomUUID().toString();
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		final Long id = 1L;
		
		UserDto dto = UserDto.builder()
				.firstName(firstName)
				.lastName(lastName)
				.userId(userId)
				.email("user1@gmail.com")
				.password("abcdefghijklmn")
				.height(height)
				.weight(weight)
				.age(age)
				.build();
		
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(null));

		try {
			// When
			userServiceImpl.updateUser(dto, userId);
		} catch (IllegalArgumentException e) {
			
			assertThat(e.getMessage().equals(USER_NOT_FOUND_EXECP)).isTrue();
		}
		
	}
}
