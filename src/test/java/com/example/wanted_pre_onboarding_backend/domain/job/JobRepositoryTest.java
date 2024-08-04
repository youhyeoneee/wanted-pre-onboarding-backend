package com.example.wanted_pre_onboarding_backend.domain.job;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {

	@Autowired
	private JobRepository jobRepository;

	@Test
	@DisplayName("회사 아이디로 채용공고 찾기 - 성공 : 4개")
	void findByCompanyId4() {
		// given
		int companyId = 1;

		// when
		List<Job> result = jobRepository.findByCompanyId(companyId);

		// then
		assertEquals(4, result.size());
	}

	@Test
	@DisplayName("회사 아이디로 채용공고 찾기 - 성공 : 3개")
	void findByCompanyId3() {
		// given
		int companyId = 2;

		// when
		List<Job> result = jobRepository.findByCompanyId(companyId);

		// then
		assertEquals(3, result.size());
	}

	@Test
	@DisplayName("회사 아이디로 채용공고 찾기 - 성공 : 0개")
	void findByCompanyId0() {
		// given
		int companyId = 0;

		// when
		List<Job> result = jobRepository.findByCompanyId(companyId);

		// then
		assertEquals(0, result.size());
	}
}