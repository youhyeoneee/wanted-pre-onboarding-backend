package com.example.wanted_pre_onboarding_backend.domain.job.exception;

public class JobNotFoundException extends RuntimeException {
	private String message;

	public JobNotFoundException(Integer jobId) {
		this.message = jobId + "번 채용공고가 존재하지 않습니다.";
	}

	@Override
	public String getMessage() {
		return message;
	}
}
