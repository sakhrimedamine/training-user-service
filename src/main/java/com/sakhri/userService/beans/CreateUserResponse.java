package com.sakhri.userService.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse {
	private String firstName;
	private String lastName;
	private String email;
	private String userId;
}