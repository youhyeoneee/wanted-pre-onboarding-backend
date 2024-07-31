package com.example.wanted_pre_onboarding_backend.domain.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wanted_pre_onboarding_backend.domain.company.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;


@ExtendWith(MockitoExtension.class)
class JobServiceTest {

	@InjectMocks
	private JobService jobService;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private CompanyRepository companyRepository;

	private RegisterJobRequestDto dto;
	private Job savedJob;

	@BeforeEach
	void setUp() {
		dto = new RegisterJobRequestDto();
		dto.setCompanyId(1);
		dto.setPosition("백엔드 주니어 개발자");
		dto.setReward(1000000);
		dto.setDetail("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..");
		dto.setSkill("Python");

		savedJob = dto.toEntity();
	}

	@Test
	@DisplayName("채용공고 등록 - 성공")
	void saveJobSuccess() {
		// given
		when(companyRepository.existsById(dto.getCompanyId())).thenReturn(true);
		when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

		// when
		Job result = jobService.saveJob(dto);

		// then
		assertNotNull(result);
		assertEquals(savedJob.getId(), result.getId());
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 존재하지 않는 회사 아이디")
	void saveJobFailure() {
		// given
		when(companyRepository.existsById(dto.getCompanyId())).thenReturn(false);

		// when, then
		assertThrows(CompanyNotFoundException.class, () -> {
			jobService.saveJob(dto);
		});
	}
}
