package com.example.wanted_pre_onboarding_backend.domain.job_application_history.service;

import org.springframework.stereotype.Service;

import com.example.wanted_pre_onboarding_backend.domain.job_application_history.repository.JobApplicaionHistoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobApplicationHistoryService {

	private JobApplicaionHistoryRepository jobApplicaionHistoryRepository;

	public boolean isDuplicatedApplication(int jobId, int userId) {
		return jobApplicaionHistoryRepository.findByJobIdAndUserId(jobId, userId).isPresent();
	}
}
