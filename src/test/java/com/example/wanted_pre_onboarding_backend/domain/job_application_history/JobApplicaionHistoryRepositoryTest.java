package com.example.wanted_pre_onboarding_backend.domain.job_application_history;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobApplicaionHistoryRepositoryTest {

	@Autowired
	private JobApplicaionHistoryRepository jobApplicaionHistoryRepository;

	@Test
	@DisplayName("채용공고 아이디와 유저 아이디로 지원내역 찾기 : 있음")
	void findByJobIdAndUserIdExist() {
		// given
		int jobId = 17;
		int userId = 1;

		// when
		Optional<JobApplicationHistory> result = jobApplicaionHistoryRepository.findByJobIdAndUserId(jobId, userId);

		// then
		assertTrue(result.isPresent());
	}

	@Test
	@DisplayName("채용공고 아이디와 유저 아이디로 지원내역 찾기 : 없음")
	void findByJobIdAndUserIdNotExist() {
		// given
		int jobId = 1;
		int userId = 1;

		// when
		Optional<JobApplicationHistory> result = jobApplicaionHistoryRepository.findByJobIdAndUserId(jobId, userId);

		// then
		assertFalse(result.isPresent());
	}
}