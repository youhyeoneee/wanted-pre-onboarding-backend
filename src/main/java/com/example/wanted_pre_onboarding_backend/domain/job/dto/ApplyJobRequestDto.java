package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplyJobRequestDto {
	@JsonProperty("user_id")
	@Min(value = 1, message = "유저 아이디는 1이상의 숫자여야 합니다.")
	private Integer userId;

	@JsonProperty("job_id")
	@Min(value = 1, message = "채용공고 아이디는 1이상의 숫자여야 합니다.")
	private Integer jobId;
}
