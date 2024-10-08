package com.example.wanted.domain.job.dto;

import com.example.wanted.domain.company.entity.Company;
import com.example.wanted.domain.job.entity.Job;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterJobRequestDto {

	@NotNull(message = "회사 아이디는 필수 요소입니다.")
	@Min(value = 1, message = "회사 아이디는 1이상의 숫자여야 합니다.")
	@JsonProperty("company_id")
	private Integer companyId;

	@NotBlank(message = "포지션은 필수 요소입니다.")
	private String position;

	@Min(value = 0, message = "보상금은 0이상의 숫자여야 합니다.")
	private int reward;

	private String detail;
	private String skill;

	public Job toEntity(Company company) {
		return new Job(company, position, reward, detail, skill);
	}
}
