package com.example.wanted_pre_onboarding_backend.domain.job;

import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class JobService {

    private JobRepository jobRepository;

    public Job saveJob(RegisterJobRequestDto jobRequestDto) {
        Job job = jobRequestDto.toEntity();
        Job savedJob = jobRepository.save(job);
        log.info("Saved job ID: " + savedJob.getId());
        return savedJob;
    }

}
