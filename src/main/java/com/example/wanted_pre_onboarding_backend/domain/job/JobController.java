package com.example.wanted_pre_onboarding_backend.domain.job;

import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
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
        return success(savedJob);
    }

    @PatchMapping("/{jobId}")
    public ApiUtils.ApiResult updateJob(@PathVariable Integer jobId, @Valid @RequestBody UpdateJobRequestDto jobRequestDto) {
        Job updatedJob = jobService.updateJob(jobId, jobRequestDto);
        return success(updatedJob);
    }
}
