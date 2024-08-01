package com.example.wanted_pre_onboarding_backend.domain.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wanted_pre_onboarding_backend.domain.company.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

	@InjectMocks
	private JobService jobService;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private CompanyRepository companyRepository;

	private RegisterJobRequestDto registerJobRequestDto;
	private UpdateJobRequestDto updateJobRequestDto;
	private Job savedJob;

	@BeforeEach
	void setUp() {
		registerJobRequestDto = new RegisterJobRequestDto();
		registerJobRequestDto.setCompanyId(1);
		registerJobRequestDto.setPosition("백엔드 주니어 개발자");
		registerJobRequestDto.setReward(1000000);
		registerJobRequestDto.setDetail("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..");
		registerJobRequestDto.setSkill("Python");

		savedJob = registerJobRequestDto.toEntity();

		updateJobRequestDto = new UpdateJobRequestDto(
			"백엔드 주니어 개발자",
			1500000,
			"원티드랩에서 백엔드 주니어 개발자를 \"적극\" 채용합니다. 자격요건은..",
			"Python"
		);
	}

	@Test
	@DisplayName("채용공고 등록 - 성공")
	void saveJobSuccess() {
		// given
		when(companyRepository.existsById(registerJobRequestDto.getCompanyId())).thenReturn(true);
		when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

		// when
		Job result = jobService.saveJob(registerJobRequestDto);

		// then
		assertNotNull(result);
		assertEquals(savedJob.getId(), result.getId());
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 존재하지 않는 회사 아이디")
	void saveJobFailure() {
		// given
		when(companyRepository.existsById(registerJobRequestDto.getCompanyId())).thenReturn(false);

		// when, then
		assertThrows(CompanyNotFoundException.class, () -> {
			jobService.saveJob(registerJobRequestDto);
		});
	}

	@Test
	@DisplayName("채용공고 수정 - 성공")
	void updateJobSuccess() {
		// given
		Integer jobId = 3;

		when(jobRepository.findById(jobId)).thenReturn(Optional.of(savedJob));
		when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

		// when
		Job result = jobService.updateJob(jobId, updateJobRequestDto);

		// then
		assertNotNull(result);
		assertEquals(updateJobRequestDto.getPosition(), result.getPosition());
		assertEquals(updateJobRequestDto.getReward(), result.getReward());
		assertEquals(updateJobRequestDto.getDetail(), result.getDetail());
		assertEquals(updateJobRequestDto.getSkill(), result.getSkill());
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 존재하지 않는 공고 아이디")
	void updateJobFailure() {
		// given
		Integer jobId = 1;
		when(jobRepository.findById(jobId)).thenReturn(java.util.Optional.empty());

		// when, then
		assertThrows(JobNotFoundException.class, () -> {
			jobService.updateJob(jobId, updateJobRequestDto);
		});
	}
}
