package com.example.wanted.domain.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wanted.domain.company.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
