package com.example.wanted_pre_onboarding_backend.domain.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wanted_pre_onboarding_backend.domain.company.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
