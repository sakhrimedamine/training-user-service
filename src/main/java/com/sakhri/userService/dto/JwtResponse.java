package com.sakhri.userService.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 3041250305017142997L;

	private String userId;
	private String token;
	private String firstName;
	private String lastName;
	private Short age;
	private Short height;
	private Short weight;
	private String role;

}