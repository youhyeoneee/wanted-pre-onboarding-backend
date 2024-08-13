package com.example.wanted.domain.job.controller;

import static com.example.wanted.global.util.ApiUtils.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.wanted.domain.job.dto.ApplyJobRequestDto;
import com.example.wanted.domain.job.dto.ApplyJobResponseDto;
import com.example.wanted.domain.job.dto.JobDetailResponseDto;
import com.example.wanted.domain.job.dto.JobInfoResponseDto;
import com.example.wanted.domain.job.dto.JobResponseDto;
import com.example.wanted.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted.domain.job.entity.Job;
import com.example.wanted.domain.job.exception.JobApplicationDuplicatedException;
import com.example.wanted.domain.job.service.JobService;
import com.example.wanted.domain.job_application_history.entity.JobApplicationHistory;
import com.example.wanted.domain.job_application_history.service.JobApplicationHistoryService;
import com.example.wanted.global.util.ApiUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/jobs")
@Slf4j
@AllArgsConstructor
public class JobController {

	private JobService jobService;
	private JobApplicationHistoryService jobApplicationHistoryService;

	@GetMapping
	public ApiUtils.ApiResult getJobList(
		@RequestParam(value = "search", required = false) String searchKeyword) {
		List<Job> jobs = jobService.findAllJob();

		if (searchKeyword != null) {
			log.info("search : " + searchKeyword);
			jobs = jobService.filterJobsBySearchKeyword(jobs, searchKeyword);
		}

		List<JobInfoResponseDto> jobInfoResponseDtos = jobs.stream()
			.map(JobInfoResponseDto::new)
			.collect(Collectors.toList());
		return success(jobInfoResponseDtos);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiUtils.ApiResult registerJob(@Valid @RequestBody RegisterJobRequestDto jobRequestDto) {
		Job savedJob = jobService.saveJob(jobRequestDto);
		JobResponseDto jobResponseDto = jobService.createJobResponseDto(savedJob);
		return success(jobResponseDto);
	}

	@GetMapping("/{jobId}")
	public ApiUtils.ApiResult getJobDetail(@PathVariable Integer jobId) {
		Job job = jobService.findJobById(jobId);
		Integer companyId = job.getCompany().getId();
		List<Job> jobsByCompanyId = jobService.findJobsByCompanyId(companyId);
		List<Integer> otherJobIds = jobService.getOtherJobIds(jobsByCompanyId, job);
		JobDetailResponseDto jobDetailResponseDto = new JobDetailResponseDto(job, otherJobIds);
		return success(jobDetailResponseDto);
	}

	@PatchMapping("/{jobId}")
	public ApiUtils.ApiResult updateJob(@PathVariable Integer jobId,
		@Valid @RequestBody UpdateJobRequestDto jobRequestDto) {
		Job updatedJob = jobService.updateJob(jobId, jobRequestDto);
		JobResponseDto jobResponseDto = jobService.createJobResponseDto(updatedJob);
		return success(jobResponseDto);
	}

	@DeleteMapping("/{jobId}")
	public ApiUtils.ApiResult<String> deleteJob(@PathVariable Integer jobId) {
		jobService.deleteJob(jobId);
		return success(jobId + "번 채용공고가 삭제되었습니다.");
	}

	@PostMapping("/apply")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiUtils.ApiResult applyJob(@Valid @RequestBody ApplyJobRequestDto applyJobRequestDto) {
		int jobId = applyJobRequestDto.getJobId();
		int userId = applyJobRequestDto.getUserId();

		boolean isDuplicated = jobApplicationHistoryService.isDuplicatedApplication(jobId, userId);
		if (isDuplicated) {
			throw new JobApplicationDuplicatedException();
		}
		JobApplicationHistory jobApplicationHistory = jobService.saveJobApplicationHistory(
			jobId,
			userId);
		ApplyJobResponseDto applyJobResponseDto = jobService.createApplyJobResponseDto(
			jobApplicationHistory);
		return success(applyJobResponseDto);
	}
}
