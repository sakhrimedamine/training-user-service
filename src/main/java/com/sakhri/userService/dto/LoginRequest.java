package com.sakhri.userService.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest implements Serializable{

	private static final long serialVersionUID = -8204236433750444502L;
	
	private String email;
	private String password;
}
	