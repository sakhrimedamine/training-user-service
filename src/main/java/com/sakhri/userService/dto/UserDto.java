package com.sakhri.userService.dto;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto implements Serializable{

	private static final long serialVersionUID = -2071875941398449647L;
	
	private String firstName;
	private String lastName;
	private String userId;
	private String password;
	private String encryptedPassword;
	private String email;
	private Short age;
	private Short height;
	private Short weight;
	private String role;

}
