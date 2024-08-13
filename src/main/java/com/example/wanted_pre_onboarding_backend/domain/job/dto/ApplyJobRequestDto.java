package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApplyJobRequestDto {

	@Min(value = 1, message = "유저 아이디는 1이상의 숫자여야 합니다.")
	private Integer userId;

	@Min(value = 1, message = "채용공고 아이디는 1이상의 숫자여야 합니다.")
	private Integer jobId;
}
