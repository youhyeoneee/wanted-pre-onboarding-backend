package com.example.wanted_pre_onboarding_backend.domain.job.dto;

import java.time.LocalDateTime;

import com.example.wanted_pre_onboarding_backend.domain.job_application_history.JobApplicationHistory;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApplyJobResponseDto {
	private int id;
	private Integer jobId;
	private Integer userId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	public ApplyJobResponseDto(JobApplicationHistory jobApplicationHistory) {
		this.id = jobApplicationHistory.getId();
		this.jobId = jobApplicationHistory.getJob().getId();
		this.userId = jobApplicationHistory.getUser().getId();
		this.createdAt = jobApplicationHistory.getCreatedAt();
	}
}
