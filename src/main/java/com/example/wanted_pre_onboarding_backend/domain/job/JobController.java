package com.example.wanted_pre_onboarding_backend.domain.job;

import com.example.wanted_pre_onboarding_backend.domain.job.dto.JobDetailResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.JobInfoResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.JobResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.global.util.ApiUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.wanted_pre_onboarding_backend.global.util.ApiUtils.success;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@Slf4j
@AllArgsConstructor
public class JobController {

    private JobService jobService;

    @GetMapping
    public ApiUtils.ApiResult getJobList() {
        List<Job> jobs = jobService.findAllJob();
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
    public ApiUtils.ApiResult updateJob(@PathVariable Integer jobId, @Valid @RequestBody UpdateJobRequestDto jobRequestDto) {
        Job updatedJob = jobService.updateJob(jobId, jobRequestDto);
        JobResponseDto jobResponseDto = jobService.createJobResponseDto(updatedJob);
        return success(jobResponseDto);
    }

    @DeleteMapping("/{jobId}")
    public ApiUtils.ApiResult<String> deleteJob(@PathVariable Integer jobId) {
        jobService.deleteJob(jobId);
        return success(jobId + "번 채용공고가 삭제되었습니다.");
    }
}
