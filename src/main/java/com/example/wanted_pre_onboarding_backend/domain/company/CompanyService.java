package com.example.wanted_pre_onboarding_backend.domain.company;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;

    public boolean existsByCompanyId(int company_id) {
        return companyRepository.existsById(company_id);
    }
}
