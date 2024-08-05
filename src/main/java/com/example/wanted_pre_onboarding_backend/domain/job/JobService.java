package com.example.wanted_pre_onboarding_backend.domain.job;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.wanted_pre_onboarding_backend.domain.company.Company;
import com.example.wanted_pre_onboarding_backend.domain.company.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.JobResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;

import jakarta.transaction.Transactional;
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
        Company company = companyRepository.findById(companyId)
            .orElseThrow(CompanyNotFoundException::new);
        Job job = jobRequestDto.toEntity(company);
        return jobRepository.save(job);
    }

    public JobResponseDto createJobResponseDto(Job job) {
        return new JobResponseDto(job);
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

    public List<Job> findAllJob() {
        return jobRepository.findAll();
    }

    public Job findJobById(Integer jobId) {
        return jobRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));
    }

    public List<Job> findJobsByCompanyId(Integer companyId) {
        companyRepository.findById(companyId).orElseThrow(CompanyNotFoundException::new);
        return jobRepository.findByCompanyId(companyId);
    }

    public List<Integer> getOtherJobIds(List<Job> jobs, Job job) {
        return jobs.stream().filter(j -> !j.equals(job))
            .map(Job::getId).toList();
    }

    public List<Job> filterJobsBySearchKeyword(List<Job> jobs, String searchKeyword) {
        return jobs.stream()
            .filter(job -> {
                Company company = job.getCompany();
                return company.getCompanyName().contains(searchKeyword) ||
                    company.getNation().contains(searchKeyword) ||
                    company.getArea().contains(searchKeyword) ||
                    job.getPosition().contains(searchKeyword) ||
                    job.getSkill().contains(searchKeyword);
            }).toList();
	}
}
