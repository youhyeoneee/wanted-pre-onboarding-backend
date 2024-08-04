package com.example.wanted_pre_onboarding_backend.domain.job;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.wanted_pre_onboarding_backend.domain.company.Company;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.JobResponseDto;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.CompanyNotFoundException;
import com.example.wanted_pre_onboarding_backend.domain.job.exception.JobNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(JobController.class)
class JobControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JobService jobService;

	@Autowired
	private ObjectMapper objectMapper;

	private RegisterJobRequestDto registerJobRequestDto;
	private JobResponseDto jobResponseDto;
	private UpdateJobRequestDto updateJobRequestDto;
	private Job savedJob;
	private Company company;
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
		jobResponseDto = new JobResponseDto(savedJob);


		updateJobRequestDto = new UpdateJobRequestDto(
			"백엔드 주니어 개발자",
			1500000,
			"원티드랩에서 백엔드 주니어 개발자를 \"적극\" 채용합니다. 자격요건은..",
			"Python"
		);
	}

	@Test
	@DisplayName("채용공고 등록 - 성공")
	void registerJobSuccess() throws Exception {
		// given
		when(jobService.saveJob(any(RegisterJobRequestDto.class))).thenReturn(savedJob);
		when(jobService.createJobResponseDto(any(Job.class))).thenReturn(jobResponseDto);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerJobRequestDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response.id").value(1))
			.andExpect(jsonPath("$.response.companyId").value(registerJobRequestDto.getCompanyId()))
			.andExpect(jsonPath("$.response.position").value(registerJobRequestDto.getPosition()))
			.andExpect(jsonPath("$.response.reward").value(registerJobRequestDto.getReward()))
			.andExpect(jsonPath("$.response.detail").value(registerJobRequestDto.getDetail()))
			.andExpect(jsonPath("$.response.skill").value(registerJobRequestDto.getSkill()))
            .andExpect(jsonPath("$.response.createdAt").isNotEmpty())
			.andExpect(jsonPath("$.response.updatedAt").isNotEmpty());
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 보상금 필드에 문자 입력")
	void registerJobFailureByReward1() throws Exception {
		// given
		String invalidJson = "{ \"companyId\": 1, \"position\": \"백엔드 주니어 개발자\", \"reward\": \"aaaaa\", \"detail\": "
			+ "\"원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..\", \"skill\": \"Python\" }";

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 보상금 필드에 0미만의 숫자 입력")
	void registerJobFailureByReward2() throws Exception {
		// given
		registerJobRequestDto.setReward(-1);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerJobRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message.reward").value("보상금은 0이상의 숫자여야 합니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 회사 아이디 필드에 문자 입력")
	void registerJobFailureByCompanyId1() throws Exception {
		// given
		String invalidJson = "{ \"companyId\": \"abc\", \"position\": \"백엔드 주니어 개발자\", \"reward\": 1000000, \"detail\": "
			+ "\"원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..\", \"skill\": \"Python\" }";

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 회사 아이디 필드에 0이하의 숫자 입력")
	void registerJobFailureByCompanyId2() throws Exception {
		// given
		registerJobRequestDto.setCompanyId(0);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerJobRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message.companyId").value("회사 아이디는 1이상의 숫자여야 합니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 회사 아이디 필드에 빈 문자열 입력")
	void registerJobFailureByCompanyId3() throws Exception {
		// given
		String invalidJson = "{ \"companyId\": \"\", \"position\": \"백엔드 주니어 개발자\", \"reward\": 1000000, \"detail\": "
			+ "\"원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..\", \"skill\": \"Python\" }";

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message.companyId").value("회사 아이디는 필수 요소입니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 존재하지 않는 회사 아이디")
	void registerJobFailureByCompanyId4() throws Exception {
		// given
		when(jobService.saveJob(any(RegisterJobRequestDto.class))).thenThrow(CompanyNotFoundException.class);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerJobRequestDto)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message").value("회사가 존재하지 않습니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("NOT_FOUND"));
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 포지션 필드에 빈 문자열 입력")
	void registerJobFailureByPosition() throws Exception {
		// given
		registerJobRequestDto.setPosition("");

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerJobRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message.position").value("포지션은 필수 요소입니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 수정 - 성공")
	void updateJobSuccess() throws Exception {
		// given
		int jobId = 1;
		Job updatedJob = new Job(company,
			updateJobRequestDto.getPosition(),
			updateJobRequestDto.getReward(),
			updateJobRequestDto.getDetail(),
			updateJobRequestDto.getSkill());

		jobResponseDto = new JobResponseDto(updatedJob);
		when(jobService.updateJob(eq(jobId), any(UpdateJobRequestDto.class))).thenReturn(updatedJob);
		when(jobService.createJobResponseDto(updatedJob)).thenReturn(jobResponseDto);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobs/" + jobId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateJobRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response.position").value(updateJobRequestDto.getPosition()))
			.andExpect(jsonPath("$.response.reward").value(updateJobRequestDto.getReward()))
			.andExpect(jsonPath("$.response.detail").value(updateJobRequestDto.getDetail()))
			.andExpect(jsonPath("$.response.skill").value(updateJobRequestDto.getSkill()));
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 채용공고 아이디에 문자 입력")
	void updateJobFailureByJobId() throws Exception {
		// given
		String jobId = "aaa";

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobs/" + jobId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateJobRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 존재하지 않는 채용공고 아이디")
	void updateJobFailureByJobId2() throws Exception {
		// given
		int jobId = 0;
		when(jobService.updateJob(eq(jobId), any(UpdateJobRequestDto.class))).thenThrow(new JobNotFoundException(jobId));

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobs/" + jobId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateJobRequestDto)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message").value(jobId + "번 채용공고가 존재하지 않습니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("NOT_FOUND"));
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 보상금 필드에 문자 입력")
	void updateJobFailureByReward1() throws Exception {
		// given
		String invalidJson = "{\"position\": \"백엔드 주니어 개발자\", \"reward\": \"aaaaa\", \"detail\": "
			+ "\"원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..\", \"skill\": \"Python\" }";

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobs/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 보상금 필드에 0미만의 숫자 입력")
	void updateJobFailureByReward2() throws Exception {
		// given
		updateJobRequestDto.setReward(-1);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobs/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateJobRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message.reward").value("보상금은 0이상의 숫자여야 합니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 포지션 필드에 빈 문자열 입력")
	void updateJobFailureByPosition() throws Exception {
		// given
		updateJobRequestDto.setPosition("");

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.patch("/api/jobs/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateJobRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message.position").value("포지션은 필수 요소입니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("BAD_REQUEST"));
	}

	@Test
	@DisplayName("채용공고 삭제 - 성공")
	void deleteJobSuccess() throws Exception {
		// given
		int jobId = 1;
		doNothing().when(jobService).deleteJob(eq(jobId));

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/jobs/" + jobId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response").value(jobId + "번 채용공고가 삭제되었습니다."));
	}

	@Test
	@DisplayName("채용공고 수정 - 실패 : 존재하지 않는 채용공고 아이디")
	void deleteJobFailureByJobId() throws Exception {
		// given
		int jobId = 0;
		doThrow(new JobNotFoundException(jobId)).when(jobService).deleteJob(eq(jobId));

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/jobs/" + jobId))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message").value(jobId + "번 채용공고가 존재하지 않습니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("NOT_FOUND"));
	}

	@Test
	@DisplayName("채용공고 목록 조회 - 성공 : 2개 리스트")
	void getJobListSuccess() throws Exception {
		// given
		Job job1 = new Job(new Company(1, "원티드랩", "한국", "서울"),
			"백엔드 주니어 개발자", 1000000, "세부사항1", "Python");
		Job job2 = new Job(new Company(2, "네이버", "한국", "판교"),
			"프론트엔드 주니어 개발자", 1200000, "세부사항2", "JavaScript");
		setField(job1, "id", 1);
		setField(job2, "id", 2);
		List<Job> jobs = Arrays.asList(job1, job2);
		when(jobService.findAllJob()).thenReturn(jobs);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/jobs"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response[0].jobId").value(1))
			.andExpect(jsonPath("$.response[0].companyName").value("원티드랩"))
			.andExpect(jsonPath("$.response[0].nation").value("한국"))
			.andExpect(jsonPath("$.response[0].area").value("서울"))
			.andExpect(jsonPath("$.response[0].position").value("백엔드 주니어 개발자"))
			.andExpect(jsonPath("$.response[0].reward").value(1000000))
			.andExpect(jsonPath("$.response[0].skill").value("Python"))
			.andExpect(jsonPath("$.response[1].jobId").value(2))
			.andExpect(jsonPath("$.response[1].companyName").value("네이버"))
			.andExpect(jsonPath("$.response[1].nation").value("한국"))
			.andExpect(jsonPath("$.response[1].area").value("판교"))
			.andExpect(jsonPath("$.response[1].position").value("프론트엔드 주니어 개발자"))
			.andExpect(jsonPath("$.response[1].reward").value(1200000))
			.andExpect(jsonPath("$.response[1].skill").value("JavaScript"));
	}

	@Test
	@DisplayName("채용공고 목록 조회 - 성공 : 빈 리스트")
	void getJobListFailureNoData() throws Exception {
		// given
		when(jobService.findAllJob()).thenReturn(Collections.emptyList());

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response").isEmpty());
	}

	@Test
	@DisplayName("채용공고 상세 조회 - 성공 : 회사의 다른 채용공고 2개")
	void getJobDetailSuccess() throws Exception {
		// given
		int jobId = 1;
		int companyId = 1;
		setField(savedJob, "id", jobId);
		when(jobService.findJobById(anyInt())).thenReturn(savedJob);

		Job job2 = new Job();
		Job job3 = new Job();
		setField(job2, "id", 2);
		setField(job3, "id", 3);
		List<Job> jobs = Arrays.asList(savedJob, job2, job3);
		when(jobService.findJobsByCompanyId(anyInt())).thenReturn(jobs);
		when(jobService.getOtherJobIds(jobs, savedJob)).thenReturn(Arrays.asList(2, 3));

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/jobs/" + jobId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response.jobId").value(1))
			.andExpect(jsonPath("$.response.companyName").value("원티드"))
			.andExpect(jsonPath("$.response.nation").value("한국"))
			.andExpect(jsonPath("$.response.area").value("서울"))
			.andExpect(jsonPath("$.response.position").value("백엔드 주니어 개발자"))
			.andExpect(jsonPath("$.response.reward").value(1000000))
			.andExpect(jsonPath("$.response.skill").value("Python"))
			.andExpect(jsonPath("$.response.detail").value("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은.."))
			.andExpect(jsonPath("$.response.otherJobIds[0]").value(2))
			.andExpect(jsonPath("$.response.otherJobIds[1]").value(3));
	}

	@Test
	@DisplayName("채용공고 상세 조회 - 성공 : 회사의 다른 채용공고 없음")
	void getJobDetailSuccess2() throws Exception {
		// given
		int jobId = 17;
		int companyId = 3;
		Job job17 = new Job(new Company(companyId, "구글", "미국", "뉴욕"),
			"백엔드 주니어 개발자", 1000000, "구글에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은..", "Django");
		setField(job17, "id", jobId);

		when(jobService.findJobById(anyInt())).thenReturn(job17);
		when(jobService.findJobsByCompanyId(anyInt())).thenReturn(Collections.emptyList());

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/jobs/" + jobId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response.jobId").value(jobId))
			.andExpect(jsonPath("$.response.companyName").value("구글"))
			.andExpect(jsonPath("$.response.nation").value("미국"))
			.andExpect(jsonPath("$.response.area").value("뉴욕"))
			.andExpect(jsonPath("$.response.position").value("백엔드 주니어 개발자"))
			.andExpect(jsonPath("$.response.reward").value(1000000))
			.andExpect(jsonPath("$.response.skill").value("Django"))
			.andExpect(jsonPath("$.response.detail").value("구글에서 백엔드 주니어 개발자를 '적극' 채용합니다. 자격요건은.."))
			.andExpect(jsonPath("$.response.otherJobIds").isEmpty());
	}

	@Test
	@DisplayName("채용공고 상세 조회 - 실패 : 존재하지 않는 채용공고 아이디")
	void getJobDetailFailure() throws Exception{
		// given
		int jobId = 0;
		doThrow(new JobNotFoundException(jobId)).when(jobService).findJobById(eq(jobId));

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/jobs/" + jobId))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.error.message").value(jobId + "번 채용공고가 존재하지 않습니다."))
			.andExpect(jsonPath("$.error.httpStatus").value("NOT_FOUND"));
	}

}

