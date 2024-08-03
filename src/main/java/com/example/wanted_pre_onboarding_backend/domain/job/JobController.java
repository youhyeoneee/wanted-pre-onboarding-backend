package com.example.wanted_pre_onboarding_backend.domain.job;

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


@RestController
@RequestMapping("/api/jobs")
@Slf4j
@AllArgsConstructor
public class JobController {

    private JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiUtils.ApiResult registerJob(@Valid @RequestBody RegisterJobRequestDto jobRequestDto) {
        Job savedJob = jobService.saveJob(jobRequestDto);
        JobResponseDto jobResponseDto = jobService.createJobResponseDto(savedJob);
        return success(jobResponseDto);
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
