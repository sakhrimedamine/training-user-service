package com.sakhri.userService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserFilterDto {

	private String userId;
	
	private String firstName;
	
	private String lastName;
	
	private String age;
	
	private String height;

	private String weight;

}
