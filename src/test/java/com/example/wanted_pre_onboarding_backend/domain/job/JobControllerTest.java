package com.example.wanted_pre_onboarding_backend.domain.job;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.wanted_pre_onboarding_backend.domain.job.dto.RegisterJobRequestDto;
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
	private UpdateJobRequestDto updateJobRequestDto;

	@BeforeEach
	void setUp() {
		registerJobRequestDto = new RegisterJobRequestDto();
		registerJobRequestDto.setCompanyId(1);
		registerJobRequestDto.setPosition("백엔드 주니어 개발자");
		registerJobRequestDto.setReward(1000000);
		registerJobRequestDto.setDetail("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..");
		registerJobRequestDto.setSkill("Python");


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
		Job savedJob = registerJobRequestDto.toEntity();
		when(jobService.saveJob(any(RegisterJobRequestDto.class))).thenReturn(savedJob);

		// when, then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerJobRequestDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.response.position").value("백엔드 주니어 개발자"));
	}

	@Test
	@DisplayName("채용공고 등록 - 실패 : 보상금 필드에 문자 입력")
	void registerJobFailByReward1() throws Exception {
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
	void registerJobFailByReward2() throws Exception {
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
		Job updatedJob = new Job(1,
			updateJobRequestDto.getPosition(),
			updateJobRequestDto.getReward(),
			updateJobRequestDto.getDetail(),
			updateJobRequestDto.getSkill());

		when(jobService.updateJob(eq(jobId), any(UpdateJobRequestDto.class))).thenReturn(updatedJob);

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
	void updateJobFailByJobId() throws Exception {
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
	void updateJobFailByJobId2() throws Exception {
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
	void updateJobFailByReward1() throws Exception {
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
	void updateJobFailByReward2() throws Exception {
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
	void updateJobFaulureByPosition() throws Exception {
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
	void deleteJobFailByJobId() throws Exception {
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

}

