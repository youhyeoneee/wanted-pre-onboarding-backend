package com.example.wanted_pre_onboarding_backend.domain.job.exception;

public class UserNotFoundException extends RuntimeException {

	private String message;

	public UserNotFoundException(int userId) {
		this.message = userId + "번 유저가 존재하지 않습니다.";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
