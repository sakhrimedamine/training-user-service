package com.sakhri.userService.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable{
	
	private static final long serialVersionUID = -1804762674017250963L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false, length=50)
	private String firstName;
	
	@Column(nullable=false, length=50)
	private String lastName;
	
	@Column(nullable=false, length=120, unique=true)
	private String email;
	
	@Column(nullable=false, unique=true)
	private String userId;
	
	@Column(nullable=false)	
	private String encryptedPassword;
	
	@Column(nullable=false)
	private Short age;
	
	@Column(nullable=false)
	private Short height;
	
	@Column(nullable=false)
	private Short weight;

	@Column(nullable=false)
	private String role = "User";

}