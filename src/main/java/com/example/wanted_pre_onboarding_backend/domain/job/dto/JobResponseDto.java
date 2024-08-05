package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import java.time.LocalDateTime;

import com.example.wanted_pre_onboarding_backend.domain.job.entity.Job;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JobResponseDto {
	int id;
	private Integer companyId;
	private String position;
	private int reward;
	private String detail;
	private String skill;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	public JobResponseDto(Job job) {
		this.id = job.getId();
		this.companyId = job.getCompany().getId();
		this.position = job.getPosition();
		this.reward = job.getReward();
		this.detail = job.getDetail();
		this.skill = job.getSkill();
		this.createdAt = job.getCreatedAt();
		this.updatedAt = job.getUpdatedAt();
	}
}
