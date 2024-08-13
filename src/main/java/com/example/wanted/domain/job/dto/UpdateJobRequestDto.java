package com.example.wanted.domain.job.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobRequestDto {

	@NotBlank(message = "포지션은 필수 요소입니다.")
	private String position;

	@Min(value = 0, message = "보상금은 0이상의 숫자여야 합니다.")
	private int reward;

	private String detail;
	private String skill;
}
