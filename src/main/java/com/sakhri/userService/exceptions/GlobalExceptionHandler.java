package com.sakhri.userService.exceptions;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.sakhri.userService.dto.ApiResponseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {
	
//	private static final String ERROR_MSG = "500 Internal Server Error.";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseDto> globleExcpetionHandler(Exception ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		ApiResponseDto errorDetails = new ApiResponseDto(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		log.info("ExceptionHandler Response : {}", errorDetails);
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponseDto> globleBadCredentialsExceptionHandler(BadCredentialsException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		ApiResponseDto errorDetails = new ApiResponseDto(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		log.info("ExceptionHandler Response : {}", errorDetails);
		return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponseDto> globleIllegalArgumentExceptionHandler(IllegalArgumentException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		ApiResponseDto errorDetails = new ApiResponseDto(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		log.info("ExceptionHandler Response : {}", errorDetails);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponseDto> globleDataIntegrityViolationExceptionnHandler(DataIntegrityViolationException ex, WebRequest request) {
		log.error(ex.getMessage(), ex);
		ApiResponseDto errorDetails = new ApiResponseDto(LocalDateTime.now(),"DUPLICATE_USERNAME", request.getDescription(false));
		log.info("ExceptionHandler Response : {}", errorDetails);
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto> handleException(MethodArgumentNotValidException exception, WebRequest request) {
    	ApiResponseDto errorDetails = new ApiResponseDto(LocalDateTime.now(),
				createValidationError(exception).getErrors().toString(), request.getDescription(false));
		log.error("ExceptionHandler Response : {}", errorDetails);
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private ValidationError createValidationError(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }
}