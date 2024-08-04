package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import java.util.List;

import com.example.wanted_pre_onboarding_backend.domain.job.Job;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class JobDetailResponseDto {
	int jobId;
	String companyName;
	String nation;
	String area;
	String position;
	int reward;
	String detail;
	String skill;
	List<Integer> otherJobIds;

	public JobDetailResponseDto(Job job, List<Integer> otherJobIds) {
		this.jobId = job.getId();
		this.companyName = job.getCompany().getCompanyName();
		this.nation = job.getCompany().getNation();
		this.area = job.getCompany().getArea();
		this.position = job.getPosition();
		this.reward = job.getReward();
		this.detail = job.getDetail();
		this.skill = job.getSkill();
		this.otherJobIds = otherJobIds;
	}
}
