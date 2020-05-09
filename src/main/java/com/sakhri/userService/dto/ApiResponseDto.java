package com.sakhri.userService.dto;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ApiResponseDto {
	 private LocalDateTime timestamp;
	 private String message;
	 private String details;

}