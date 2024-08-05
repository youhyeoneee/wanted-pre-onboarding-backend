package com.example.wanted_pre_onboarding_backend.domain.job_application_history.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wanted_pre_onboarding_backend.domain.job_application_history.entity.JobApplicationHistory;

public interface JobApplicaionHistoryRepository extends JpaRepository<JobApplicationHistory, Integer> {
	Optional<JobApplicationHistory> findByJobIdAndUserId(Integer jobId, Integer userId);
}
