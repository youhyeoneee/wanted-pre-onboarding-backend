package com.example.wanted_pre_onboarding_backend.domain.job;

import java.util.List;

import com.example.wanted_pre_onboarding_backend.domain.company.Company;
import com.example.wanted_pre_onboarding_backend.domain.company.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class JobService {

    private JobRepository jobRepository;
    private CompanyRepository companyRepository;

    public Job saveJob(RegisterJobRequestDto jobRequestDto) {
        int companyId = jobRequestDto.getCompanyId();
        Company company = companyRepository.findById(companyId)
            .orElseThrow(CompanyNotFoundException::new);
        Job job = jobRequestDto.toEntity(company);
        return jobRepository.save(job);
    }

    public RegisterJobResponseDto getRegisterJobResponseDto(Job job) {
        return new RegisterJobResponseDto(job);
    }

	public Job updateJob(Integer jobId, UpdateJobRequestDto jobRequestDto) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));
        job.updateJob(jobRequestDto);
		return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Integer jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));
        jobRepository.delete(job);
    }
}
