package com.sakhri.userService.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse implements Serializable{

	private static final long serialVersionUID = 7178816923053392200L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String userId;
}