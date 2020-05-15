package com.sakhri.userService.dto;


import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateUserResquest implements Serializable {

	private static final long serialVersionUID = 4233473503682283130L;

	@NotNull(message="First name cannot be null")
	@Size(min=2, message= "First name must not be less than two characters")
	private String firstName;
	
	@NotNull(message="Last name cannot be null")
	@Size(min=2, message= "Last name must not be less than two characters")
	private String lastName;
	
	@NotNull(message="Age cannot be null")
	@Positive(message="Age cannot be negative")
	@Max(value= 100, message="Age cannot be grater than 100")
	private Short age;
	
	@NotNull(message="Height cannot be null")
	@Positive(message="Height cannot be negative")
	@Max(value= 300, message="Height cannot be grater than 300")
	private Short height;
	
	@NotNull(message="Weight cannot be null")	
	@Positive(message="Weight cannot be negative")
	@Max(value= 300, message="Weight cannot be grater than 300")
	private Short weight;
}
