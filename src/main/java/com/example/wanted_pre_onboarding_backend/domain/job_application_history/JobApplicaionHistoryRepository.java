package com.example.wanted_pre_onboarding_backend.domain.job_application_history;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicaionHistoryRepository extends JpaRepository<JobApplicationHistory, Integer> {
	Optional<JobApplicationHistory> findByJobIdAndUserId(Integer jobId, Integer userId);
}
