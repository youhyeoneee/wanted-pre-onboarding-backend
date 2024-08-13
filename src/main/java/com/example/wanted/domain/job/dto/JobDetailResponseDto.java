package com.example.wanted.domain.job.dto;

import java.util.List;

import com.example.wanted.domain.job.entity.Job;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
