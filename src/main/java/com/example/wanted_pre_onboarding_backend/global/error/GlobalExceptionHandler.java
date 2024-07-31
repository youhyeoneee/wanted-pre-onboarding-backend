package com.example.wanted_pre_onboarding_backend.global.error;


import com.example.wanted_pre_onboarding_backend.global.util.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import static com.example.wanted_pre_onboarding_backend.global.util.ApiUtils.error;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiUtils.ApiResult<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException errors) {
        Map<String, String> errorMessages = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String errorField = error.getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.put(errorField, errorMessage);
        }
        return error(errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiUtils.ApiResult<String> handleSQLException(SQLIntegrityConstraintViolationException e) {
        String errorMessage = e.getMessage();
        return error(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiUtils.ApiResult<String> handleHttpException(HttpMessageNotReadableException e) {
        String errorMessage = e.getMessage();
        return error(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
