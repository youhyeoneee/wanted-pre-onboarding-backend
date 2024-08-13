package com.example.wanted_pre_onboarding_backend.domain.job.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

	@InjectMocks
	private JobService jobService;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private CompanyRepository companyRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JobApplicaionHistoryRepository jobApplicaionHistoryRepository;

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
	@DisplayName("JobResponseDto 생성 - 성공")
	void createJobResponseDtoSuccess() {
		// when
		JobResponseDto responseDto = jobService.createJobResponseDto(savedJob);

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

	@Test
	@DisplayName("채용공고 목록 조회 - 성공")
	void findAllJobSuccess() {
		// given
		List<Job> jobs = Arrays.asList(
			new Job(new Company(1, "원티드랩", "한국", "서울"), "백엔드 주니어 개발자", 1000000, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"Python"),
			new Job(new Company(2, "네이버", "한국", "판교"), "프론트엔드 주니어 개발자", 1200000, "네이버에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"JavaScript")
		);

		when(jobRepository.findAll()).thenReturn(jobs);

		// when
		List<Job> result = jobService.findAllJob();

		// then
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	@DisplayName("채용공고 아이디로 조회 - 성공")
	void findJobByIdSuccess() {
		// given
		when(jobRepository.findById(anyInt())).thenReturn(Optional.of(savedJob));

		// when
		Job job = jobService.findJobById(1);

		// then
		assertNotNull(job);
		assertEquals(1, job.getId());
	}

	@Test
	@DisplayName("채용공고 아이디로 조회 - 실패")
	void findJobByIdFailure() {
		// given
		when(jobRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

		// when, then
		assertThrows(JobNotFoundException.class, () -> {
			jobService.findJobById(1);
		});
	}

	@Test
	@DisplayName("채용공고 목록 회사 아이디로 조회 - 성공")
	void findJobsByCompanyIdSuccess2() {
		// given
		Job job = new Job();
		List<Job> jobs = Arrays.asList(savedJob, job);
		when(jobRepository.findByCompanyId(anyInt())).thenReturn(jobs);
		when(companyRepository.findById(anyInt())).thenReturn(Optional.of(company));

		// when
		List<Job> result = jobService.findJobsByCompanyId(1);

		// then
		assertEquals(2, result.size());
	}

	@Test
	@DisplayName("채용공고 목록 회사 아이디로 조회 - 실패 : 존재하지 않는 회사 아이디")
	void findJobsByCompanyIdSuccess0() {
		// given
		when(companyRepository.findById(anyInt())).thenReturn(Optional.empty());

		// when, then
		assertThrows(CompanyNotFoundException.class, () -> {
			jobService.findJobsByCompanyId(1);
		});
	}

	@Test
	@DisplayName("나와 다른 채용공고 아이디 추출 - 성공 : 2개")
	void getOtherJobIdsSuccess2() {
		// given
		Job job = new Job();
		Job job2 = new Job();
		setField(job, "id", 2);
		setField(job2, "id", 3);
		List<Job> jobs = Arrays.asList(savedJob, job, job2);

		// when
		List<Integer> result = jobService.getOtherJobIds(jobs, savedJob);

		// then
		assertEquals(2, result.size());
		assertEquals(Arrays.asList(2, 3), result);
	}

	@Test
	@DisplayName("나와 다른 채용공고 아이디 추출 - 성공 : 0개")
	void getOtherJobIdsSuccess0() {
		// given
		List<Job> jobs = Arrays.asList(savedJob);

		// when
		List<Integer> result = jobService.getOtherJobIds(jobs, savedJob);

		// then
		assertEquals(0, result.size());
	}

	@Test
	@DisplayName("채용공고 목록 검색어로 필터링 - 성공 : 회사명")
	void filterJobsBySearchKeywordSuccessCompanyName() {
		// given
		List<Job> jobs = Arrays.asList(
			new Job(new Company(1, "원티드랩", "한국", "서울"), "백엔드 주니어 개발자", 1000000, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"Python"),
			new Job(new Company(2, "네이버", "한국", "판교"), "프론트엔드 주니어 개발자", 1200000, "네이버에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"JavaScript")
		);
		String searchKeyword = "원티드";

		// when
		List<Job> result = jobService.filterJobsBySearchKeyword(jobs, searchKeyword);

		// then
		assertEquals(1, result.size());
	}

	@Test
	@DisplayName("채용공고 목록 검색어로 필터링 - 성공 : 국가")
	void filterJobsBySearchKeywordSuccessNation() {
		// given
		List<Job> jobs = Arrays.asList(
			new Job(new Company(1, "원티드랩", "한국", "서울"), "백엔드 주니어 개발자", 1000000, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"Python"),
			new Job(new Company(2, "네이버", "한국", "판교"), "프론트엔드 주니어 개발자", 1200000, "네이버에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"JavaScript")
		);
		String searchKeyword = "한국";

		// when
		List<Job> result = jobService.filterJobsBySearchKeyword(jobs, searchKeyword);

		// then
		assertEquals(2, result.size());
	}

	@Test
	@DisplayName("채용공고 목록 검색어로 필터링 - 성공 : 지역")
	void filterJobsBySearchKeywordSuccessArea() {
		// given
		List<Job> jobs = Arrays.asList(
			new Job(new Company(1, "원티드랩", "한국", "서울"), "백엔드 주니어 개발자", 1000000, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"Python"),
			new Job(new Company(2, "네이버", "한국", "판교"), "프론트엔드 주니어 개발자", 1200000, "네이버에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"JavaScript")
		);
		String searchKeyword = "판교";

		// when
		List<Job> result = jobService.filterJobsBySearchKeyword(jobs, searchKeyword);

		// then
		assertEquals(1, result.size());
	}

	@Test
	@DisplayName("채용공고 목록 검색어로 필터링 - 성공 : 포지션")
	void filterJobsBySearchKeywordSuccessPosition() {
		// given
		List<Job> jobs = Arrays.asList(
			new Job(new Company(1, "원티드랩", "한국", "서울"), "백엔드 주니어 개발자", 1000000, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"Python"),
			new Job(new Company(2, "네이버", "한국", "판교"), "프론트엔드 주니어 개발자", 1200000, "네이버에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"JavaScript")
		);
		String searchKeyword = "백엔드";

		// when
		List<Job> result = jobService.filterJobsBySearchKeyword(jobs, searchKeyword);

		// then
		assertEquals(1, result.size());
	}

	@Test
	@DisplayName("채용공고 목록 검색어로 필터링 - 성공 : 사용기술")
	void filterJobsBySearchKeywordSuccessSkill() {
		// given
		List<Job> jobs = Arrays.asList(
			new Job(new Company(1, "원티드랩", "한국", "서울"), "백엔드 주니어 개발자", 1000000, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"Python"),
			new Job(new Company(2, "네이버", "한국", "판교"), "프론트엔드 주니어 개발자", 1200000, "네이버에서 프론트엔드 주니어 개발자를 채용합니다. 자격요건은..",
				"JavaScript")
		);
		String searchKeyword = "Python";

		// when
		List<Job> result = jobService.filterJobsBySearchKeyword(jobs, searchKeyword);

		// then
		assertEquals(1, result.size());
	}

	@Test
	@DisplayName("채용공고 지원내역 저장 - 성공")
	void saveJobApplicationHistorySuccess() {
		// given
		int jobId = 1;
		int userId = 2;
		User user = new User(userId, "test", "test");
		JobApplicationHistory jobApplicationHistory = new JobApplicationHistory(1, savedJob, user, LocalDateTime.now());
		when(jobRepository.findById(anyInt())).thenReturn(Optional.of(savedJob));
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
		when(jobApplicaionHistoryRepository.save(any(JobApplicationHistory.class))).thenReturn(jobApplicationHistory);

		// when
		JobApplicationHistory result = jobService.saveJobApplicationHistory(jobId, userId);

		// then
		assertEquals(1, result.getId());
		assertEquals(userId, result.getUser().getId());
		assertEquals(jobId, result.getJob().getId());
		assertNotNull(result.getCreatedAt());
	}

	@Test
	@DisplayName("채용공고 지원내역 저장 - 실패 : 존재하지 않는 공고 아이디")
	void saveJobApplicationHistoryFailureByJobId() {
		// given
		int jobId = 1;
		int userId = 2;
		when(jobRepository.findById(anyInt())).thenThrow(new JobNotFoundException(jobId));

		// when, then
		assertThrows(JobNotFoundException.class, () -> {
			JobApplicationHistory result = jobService.saveJobApplicationHistory(jobId, userId);
		});
	}

	@Test
	@DisplayName("채용공고 지원내역 저장 - 실패 : 존재하지 않는 유저 아이디")
	void saveJobApplicationHistoryFailureByUserId() {
		// given
		int jobId = 1;
		int userId = 2;
		when(jobRepository.findById(anyInt())).thenReturn(Optional.of(savedJob));
		when(userRepository.findById(anyInt())).thenThrow(new UserNotFoundException(userId));

		// when, then
		assertThrows(UserNotFoundException.class, () -> {
			JobApplicationHistory result = jobService.saveJobApplicationHistory(jobId, userId);
		});
	}

	@Test
	@DisplayName("ApplyJobResponseDto 생성 - 성공")
	void createApplyJobResponseDto() {
		// when
		User user = new User(2, "test", "test");
		JobApplicationHistory jobApplicationHistory = new JobApplicationHistory(1, savedJob, user, LocalDateTime.now());
		ApplyJobResponseDto responseDto = jobService.createApplyJobResponseDto(jobApplicationHistory);

		// then
		assertNotNull(responseDto);
		assertEquals(jobApplicationHistory.getId(), responseDto.getId());
		assertEquals(savedJob.getId(), responseDto.getJobId());
		assertEquals(user.getId(), responseDto.getUserId());
		assertNotNull(responseDto.getCreatedAt());
	}
}
