package com.example.wanted.domain.job.dto;

import java.time.LocalDateTime;

import com.example.wanted.domain.job_application_history.entity.JobApplicationHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
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
