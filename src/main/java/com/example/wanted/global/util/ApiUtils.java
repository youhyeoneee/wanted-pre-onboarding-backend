package com.example.wanted.global.util;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ApiUtils<T> {

	public static <T> ApiResult<T> success(T data) {
		return new ApiResult<T>(true, data, null);
	}

	public static <M> ApiResult<M> error(M message, HttpStatus httpStatus) {
		return new ApiResult(false,
			null,
			new ApiError(message, httpStatus));
	}

	@Getter
	@AllArgsConstructor
	public static class ApiResult<T> {

		boolean success;
		T response;
		ApiError error;
	}

	@Getter
	static class ApiError<M> {

		M message;
		@JsonProperty("http_status")
		HttpStatus httpStatus;

		ApiError(M message, HttpStatus httpStatus) {
			this.message = message;
			this.httpStatus = httpStatus;
		}
	}
}
