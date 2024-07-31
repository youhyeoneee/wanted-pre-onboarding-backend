package com.example.wanted_pre_onboarding_backend.domain.job;

import com.example.wanted_pre_onboarding_backend.domain.company.CompanyService;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.global.util.ApiUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.wanted_pre_onboarding_backend.global.util.ApiUtils.error;
import static com.example.wanted_pre_onboarding_backend.global.util.ApiUtils.success;


@RestController
@RequestMapping("/api/jobs")
@Slf4j
@AllArgsConstructor
public class JobController {

    private CompanyService companyService;
    private JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiUtils.ApiResult registerJob(@Valid @RequestBody RegisterJobRequestDto jobRequestDto) {
        int companyId = jobRequestDto.getCompanyId();
        if (companyService.existsByCompanyId(companyId)) {
            Job savedJob = jobService.saveJob(jobRequestDto);
            return success(savedJob);
        } else {
            throw new CompanyNotFoundException("채용공고 등록 실패");
        }
    }
}
