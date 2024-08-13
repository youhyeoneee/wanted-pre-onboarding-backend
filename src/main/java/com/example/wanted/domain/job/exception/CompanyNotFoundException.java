package com.example.wanted.domain.job.exception;

public class CompanyNotFoundException extends RuntimeException {
	private final String message = "회사가 존재하지 않습니다.";

	@Override
	public String getMessage() {
		return message;
	}
}
