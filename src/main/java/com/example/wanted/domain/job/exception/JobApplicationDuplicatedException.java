package com.example.wanted.domain.job.exception;

public class JobApplicationDuplicatedException extends RuntimeException {

	private String message = "이미 지원한 채용공고입니다.";

	@Override
	public String getMessage() {
		return message;
	}
}
