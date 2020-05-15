package com.sakhri.userService.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sakhri.userService.model.UserEntity;


@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Test
	void whenFindByFiltersReturnCorrespondingListOfUsers() {
		// Given
		final String firstName = "firstName1";
		final String lastName = "lastname1";
		final short age = 22;
		final short height = 180;
		final short weight = 88;

		
		final UserEntity user = UserEntity.builder()
				.firstName(firstName)
				.lastName(lastName)
				.userId(UUID.randomUUID().toString())
				.email("user1@gmail.com")
				.encryptedPassword("abcdefghijklmn")
				.height(height)
				.weight(weight)
				.age(age)
				.build();
		userRepository.save(user);
		
		//When
		List<UserEntity> retreivedUsersByFirstName = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
					null,firstName,null,null,null,null);
		
		List<UserEntity> retreivedUsersByLastName = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,null,lastName,null,null,null);

		List<UserEntity> retreivedUsersByAge = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,null,null,age,null,null);

		List<UserEntity> retreivedUsersByWeight = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,null,null,null,weight,null);

		List<UserEntity> retreivedUsersByHeight = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,null,null,null,null,height);
	
		List<UserEntity> retreivedUsersByFirstAndLastName = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,firstName,lastName,null,null,null);

		List<UserEntity> retreivedUsersByAgeAndHeightAndWeight = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,null,null,age,weight,height);

		List<UserEntity> retreivedUsersByIncorrectFirstName = userRepository.findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(
				null,"False",null,null,null,null);

		
		//Then
		assertEquals(retreivedUsersByFirstName.size(), 1);
		assertEquals(retreivedUsersByFirstName.get(0), user);
		
		assertEquals(retreivedUsersByLastName.size(), 1);
		assertEquals(retreivedUsersByLastName.get(0), user);

		assertEquals(retreivedUsersByAge.size(), 1);
		assertEquals(retreivedUsersByAge.get(0), user);

		assertEquals(retreivedUsersByWeight.size(), 1);
		assertEquals(retreivedUsersByWeight.get(0), user);

		assertEquals(retreivedUsersByHeight.size(), 1);
		assertEquals(retreivedUsersByHeight.get(0), user);

		assertEquals(retreivedUsersByFirstAndLastName.size(), 1);
		assertEquals(retreivedUsersByFirstAndLastName.get(0), user);

		assertEquals(retreivedUsersByAgeAndHeightAndWeight.size(), 1);
		assertEquals(retreivedUsersByAgeAndHeightAndWeight.get(0), user);

		assertEquals(retreivedUsersByIncorrectFirstName.size(), 0);

	}

	@Test
	void whenFindByEmailReturnCorrespondingListOfUsers() {
		// Given
		final String email = "user1@gmail.com";
		final UserEntity user = UserEntity.builder()
				.firstName("firstName1")
				.lastName("lastname1")
				.userId(UUID.randomUUID().toString())
				.email(email)
				.encryptedPassword("abcdefghijklmn")
				.height((short) 180)
				.weight((short) 88)
				.age((short) 14)
				.build();
		userRepository.save(user);
		//When
		UserEntity retreivedUser = userRepository.findByEmail(email);
		
		//Then
		assertEquals(user, retreivedUser);
	}

	@Test
	void whenFindByUserIdReturnCorrespondingUser() {
		// Given
		final String userId = UUID.randomUUID().toString();
		final UserEntity user = UserEntity.builder()
				.firstName("firstName1")
				.lastName("lastname1")
				.userId(userId)
				.email("user1@gmail.com")
				.encryptedPassword("abcdefghijklmn")
				.height((short) 180)
				.weight((short) 88)
				.age((short) 14)
				.build();
		userRepository.save(user);
		//When
		UserEntity retreivedUser = userRepository.findByUserId(userId);
		
		//Then
		assertEquals(user, retreivedUser);
	}

}
