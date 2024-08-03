package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import com.example.wanted_pre_onboarding_backend.domain.job.Job;

import lombok.Getter;

@Getter
public class JobInfoResponseDto {
	int companyId;
	String companyName;
	String nation;
	String area;
	String position;
	int reward;
	String skill;

	public JobInfoResponseDto(Job job) {
		this.companyId = job.getCompany().getId();
		this.companyName = job.getCompany().getCompanyName();
		this.nation = job.getCompany().getNation();
		this.area = job.getCompany().getArea();
		this.position = job.getPosition();
		this.reward = job.getReward();
		this.skill = job.getSkill();
	}
}
