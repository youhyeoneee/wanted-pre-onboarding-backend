package com.example.wanted_pre_onboarding_backend.domain.job.repository;

import java.util.List;

import com.example.wanted_pre_onboarding_backend.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
	List<Job> findByCompanyId(Integer companyId);
}
