package com.example.wanted_pre_onboarding_backend.domain.job;

import com.example.wanted_pre_onboarding_backend.domain.company.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class JobService {

    private JobRepository jobRepository;
    private CompanyRepository companyRepository;

    public Job saveJob(RegisterJobRequestDto jobRequestDto) {
        int companyId = jobRequestDto.getCompanyId();
        if (companyRepository.existsById(companyId)) {
            Job job = jobRequestDto.toEntity();
            Job savedJob = jobRepository.save(job);
            return savedJob;
        } else {
            throw new CompanyNotFoundException();
        }
    }

	public Job updateJob(Integer jobId, UpdateJobRequestDto jobRequestDto) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));
        job.updateJob(jobRequestDto);
		return jobRepository.save(job);
    }
}
