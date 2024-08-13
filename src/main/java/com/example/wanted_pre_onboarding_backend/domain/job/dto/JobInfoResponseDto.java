package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import com.example.wanted_pre_onboarding_backend.domain.job.entity.Job;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JobInfoResponseDto {
	int jobId;
	String companyName;
	String nation;
	String area;
	String position;
	int reward;
	String skill;

	public JobInfoResponseDto(Job job) {
		this.jobId = job.getId();
		this.companyName = job.getCompany().getCompanyName();
		this.nation = job.getCompany().getNation();
		this.area = job.getCompany().getArea();
		this.position = job.getPosition();
		this.reward = job.getReward();
		this.skill = job.getSkill();
	}
}
