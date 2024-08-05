package com.example.wanted_pre_onboarding_backend.domain.job_application_history.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.wanted_pre_onboarding_backend.domain.job_application_history.entity.JobApplicationHistory;
import com.example.wanted_pre_onboarding_backend.domain.job_application_history.repository.JobApplicaionHistoryRepository;
import com.example.wanted_pre_onboarding_backend.domain.job_application_history.service.JobApplicationHistoryService;

@ExtendWith(MockitoExtension.class)
class JobApplicationHistoryServiceTest {
	@InjectMocks
	private JobApplicationHistoryService jobApplicationHistoryService;

	@Mock
	private JobApplicaionHistoryRepository jobApplicaionHistoryRepository;

	@Test
	@DisplayName("처음 지원한 채용공고일 경우")
	void isDuplicatedApplicationFalse() {
		// given
		when(jobApplicaionHistoryRepository.findByJobIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.empty());

		// when
		boolean result = jobApplicationHistoryService.isDuplicatedApplication(1, 1);

		// then
		assertEquals(false, result);
	}

	@Test
	@DisplayName("이미 지원한 채용공고일 경우")
	void isDuplicatedApplicationTrue() {
		// given
		JobApplicationHistory jobApplicationHistory = new JobApplicationHistory();
		when(jobApplicaionHistoryRepository.findByJobIdAndUserId(anyInt(), anyInt())).thenReturn(Optional.of(jobApplicationHistory));

		// when
		boolean result = jobApplicationHistoryService.isDuplicatedApplication(1, 1);

		// then
		assertEquals(true, result);
	}
}