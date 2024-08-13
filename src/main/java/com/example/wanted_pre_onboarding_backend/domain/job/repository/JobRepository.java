package com.example.wanted_pre_onboarding_backend.domain.job.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wanted_pre_onboarding_backend.domain.job.entity.Job;

public interface JobRepository extends JpaRepository<Job, Integer> {

	List<Job> findByCompanyId(Integer companyId);
}
