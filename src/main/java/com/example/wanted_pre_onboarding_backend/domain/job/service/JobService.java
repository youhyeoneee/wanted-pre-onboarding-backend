package com.example.wanted_pre_onboarding_backend.domain.job.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.wanted_pre_onboarding_backend.domain.company.entity.Company;
import com.example.wanted_pre_onboarding_backend.domain.company.repository.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.ApplyJobResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.JobResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.entity.Job;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.UserNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.repository.JobRepository;
import com.example.wanted_pre_onboarding_backend.domain.job_application_history.entity.JobApplicationHistory;
import com.example.wanted_pre_onboarding_backend.domain.job_application_history.repository.JobApplicaionHistoryRepository;
import com.example.wanted_pre_onboarding_backend.domain.user.entity.User;
import com.example.wanted_pre_onboarding_backend.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class JobService {

	private JobRepository jobRepository;
	private CompanyRepository companyRepository;
	private UserRepository userRepository;
	private JobApplicaionHistoryRepository jobApplicaionHistoryRepository;

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
				return company.getCompanyName().contains(searchKeyword)
					|| company.getNation().contains(searchKeyword)
					|| company.getArea().contains(searchKeyword)
					|| job.getPosition().contains(searchKeyword)
					|| job.getSkill().contains(searchKeyword);
			}).toList();
	}

	public JobApplicationHistory saveJobApplicationHistory(int jobId, int userId) {
		Job job = findJobById(jobId);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		JobApplicationHistory jobApplicationHistory = new JobApplicationHistory(job, user);
		return jobApplicaionHistoryRepository.save(jobApplicationHistory);
	}

	public ApplyJobResponseDto createApplyJobResponseDto(
		JobApplicationHistory jobApplicationHistory) {
		return new ApplyJobResponseDto(jobApplicationHistory);
	}
}
