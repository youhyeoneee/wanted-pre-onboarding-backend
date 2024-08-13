package com.example.wanted_pre_onboarding_backend.global.error;

import static com.example.wanted_pre_onboarding_backend.global.util.ApiUtils.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobApplicationDuplicatedException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.UserNotFoundException;
import com.example.wanted_pre_onboarding_backend.global.util.ApiUtils;
import com.example.wanted_pre_onboarding_backend.global.util.StringUtils;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiUtils.ApiResult<Map<String, String>> handleValidationExceptions(
		MethodArgumentNotValidException errors) {
		Map<String, String> errorMessages = new HashMap<>();
		for (FieldError error : errors.getFieldErrors()) {
			String errorField = StringUtils.toSnakeCase(error.getField());
			String errorMessage = error.getDefaultMessage();
			errorMessages.put(errorField, errorMessage);
		}
		return error(errorMessages, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiUtils.ApiResult<String> handleSqlException(
		SQLIntegrityConstraintViolationException exception) {
		String errorMessage = exception.getMessage();
		return error(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiUtils.ApiResult<String> handleHttpException(HttpMessageNotReadableException exception) {
		String errorMessage = "잘못된 입력 형식입니다 : " + exception.getLocalizedMessage();
		return error(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({CompanyNotFoundException.class, JobNotFoundException.class,
		UserNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiUtils.ApiResult<String> handleBadRequestException(RuntimeException exception) {
		String errorMessage = exception.getMessage();
		return error(errorMessage, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiUtils.ApiResult<String> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException exception) {
		String errorMessage = exception.getMessage();
		return error(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiUtils.ApiResult<String> handleJobApplicationDuplicatedException(
		JobApplicationDuplicatedException exception) {
		String errorMessage = exception.getMessage();
		return error(errorMessage, HttpStatus.CONFLICT);
	}
}
