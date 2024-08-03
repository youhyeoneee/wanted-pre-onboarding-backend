package com.example.wanted_pre_onboarding_backend.domain.job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wanted_pre_onboarding_backend.domain.company.Company;
import com.example.wanted_pre_onboarding_backend.domain.company.CompanyRepository;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobResponseDto;
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
	private Company company;
	private Job savedJob;

	@BeforeEach
	void setUp() {
		int companyId = 1;
		registerJobRequestDto = new RegisterJobRequestDto();
		registerJobRequestDto.setCompanyId(companyId);
		registerJobRequestDto.setPosition("백엔드 주니어 개발자");
		registerJobRequestDto.setReward(1000000);
		registerJobRequestDto.setDetail("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..");
		registerJobRequestDto.setSkill("Python");

		company = new Company(companyId, "원티드", "한국", "서울");
		savedJob = registerJobRequestDto.toEntity(company);
		setField(savedJob, "id", 1);
		setField(savedJob, "createdAt", LocalDateTime.now());
		setField(savedJob, "updatedAt", LocalDateTime.now());

		updateJobRequestDto = new UpdateJobRequestDto(
			"백엔드 주니어 개발자",
			1500000,
			"원티드랩에서 백엔드 주니어 개발자를 \"적극\" 채용합니다. 자격요건은..",
			"Python"
		);
	}

	@Test
	@DisplayName("채용공고 저장 - 성공")
	void saveJobSuccess() {
		// given
		when(companyRepository.findById(registerJobRequestDto.getCompanyId())).thenReturn(Optional.ofNullable(company));
		when(jobRepository.save(any(Job.class))).thenReturn(savedJob);

		// when
		Job result = jobService.saveJob(registerJobRequestDto);

		// then
		assertNotNull(result);
		assertEquals(savedJob.getId(), result.getId());
	}

	@Test
	@DisplayName("채용공고 저장 - 실패 : 존재하지 않는 회사 아이디")
	void saveJobFailure() {
		// given
		when(companyRepository.findById(registerJobRequestDto.getCompanyId())).thenReturn(Optional.empty());

		// when, then
		assertThrows(CompanyNotFoundException.class, () -> {
			jobService.saveJob(registerJobRequestDto);
		});
	}

	@Test
	@DisplayName("RegisterJobResponseDto 생성 - 성공")
	void getRegisterJobResponseDtoSuccess() {
		// when
		RegisterJobResponseDto responseDto = jobService.getRegisterJobResponseDto(savedJob);

		// then
		assertNotNull(responseDto);
		assertEquals(savedJob.getId(), responseDto.getId());
		assertEquals(savedJob.getCompany().getId(), responseDto.getCompanyId());
		assertEquals(savedJob.getPosition(), responseDto.getPosition());
		assertEquals(savedJob.getReward(), responseDto.getReward());
		assertEquals(savedJob.getDetail(), responseDto.getDetail());
		assertEquals(savedJob.getSkill(), responseDto.getSkill());
		assertEquals(savedJob.getCreatedAt(), responseDto.getCreatedAt());
		assertEquals(savedJob.getUpdatedAt(), responseDto.getUpdatedAt());
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
		when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

		// when, then
		assertThrows(JobNotFoundException.class, () -> {
			jobService.updateJob(jobId, updateJobRequestDto);
		});
	}


	@Test
	@DisplayName("채용공고 삭제 - 성공")
	void deleteJobSuccess() {
		// given
		int jobId = 1;
		when(jobRepository.findById(jobId)).thenReturn(Optional.of(savedJob));
		doNothing().when(jobRepository).delete(savedJob);

		// when
		jobService.deleteJob(jobId);

		// then
		verify(jobRepository, times(1)).delete(savedJob);
	}

	@Test
	@DisplayName("채용공고 삭제 - 실패 : 존재하지 않는 공고 아이디")
	void deleteJobFailure() {
		// given
		int jobId = 0;
		when(jobRepository.findById(jobId)).thenReturn(java.util.Optional.empty());

		// when, then
		assertThrows(JobNotFoundException.class, () -> {
			jobService.deleteJob(jobId);
		});
		verify(jobRepository, never()).delete(savedJob);
	}
}
