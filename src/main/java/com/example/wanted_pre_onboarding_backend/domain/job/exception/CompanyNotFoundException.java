package com.example.wanted_pre_onboarding_backend.domain.job.exception;

public class CompanyNotFoundException extends RuntimeException {
	private String message;

	public CompanyNotFoundException(String message) {
		this.message = message + " : 회사가 존재하지 않습니다.";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
