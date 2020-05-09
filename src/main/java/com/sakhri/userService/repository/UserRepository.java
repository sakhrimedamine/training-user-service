package com.sakhri.userService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sakhri.userService.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	@Query("SELECT u FROM UserEntity u WHERE (:userId is null or u.userId = :userId) and "
		+ "(:firstName is null or u.firstName = :firstName) and (:lastName is null or u.lastName = :lastName) and"
		+ "(:age is null or u.age = :age) and"
		+ "(:weight is null or u.weight = :weight) and (:height is null or u.height = :height)")
	public List<UserEntity> findByIdAndFistNameAndLastNameAndAgeAndWeightAndHeight(String userId, String firstName, 
			String lastName, Short age, Short weight, Short height);
	
	public UserEntity findByEmail(String email);
	
	public UserEntity findByUserId(String userId);

}
