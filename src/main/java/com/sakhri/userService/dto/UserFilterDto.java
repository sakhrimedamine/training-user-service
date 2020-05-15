package com.sakhri.userService.dto;


import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserFilterDto implements Serializable {

	private static final long serialVersionUID = -5594134523695994288L;

	private String userId;
	
	@Size(min=2, message= "First name must not be less than two characters")
	private String firstName;
	
	@Size(min=2, message= "Last name must not be less than two characters")
	private String lastName;
	
	@Positive(message="Age cannot be negative")
	@Max(value= 100, message="Age cannot be grater than 100")
	private Short age;
	
	@Positive(message="Height cannot be negative")
	@Max(value= 300, message="Height cannot be grater than 300")
	private Short height;
	
	@Positive(message="Weight cannot be negative")
	@Max(value= 300, message="Weight cannot be grater than 300")
	private Short weight;

}
