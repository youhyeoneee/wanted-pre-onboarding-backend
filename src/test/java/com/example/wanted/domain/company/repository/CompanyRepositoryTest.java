package com.example.wanted.domain.company.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.wanted.domain.company.entity.Company;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompanyRepositoryTest {

	@Autowired
	private CompanyRepository companyRepository;

	@BeforeEach
	void setUp() {
		companyRepository.save(new Company(1, "원티드", "한국", "서울"));
	}

	@Test
	@DisplayName("회사 아이디 존재 여부 확인 - 성공")
	void existsByIdSuccess() {
		// given
		int companyId = 1;

		// when
		boolean result = companyRepository.existsById(companyId);

		// then
		assertTrue(result);
	}

	@Test
	@DisplayName("회사 아이디 존재 여부 확인 - 실패 : 존재하지 않음")
	void existsByIdFailure() {
		// given
		int companyId = 0;

		// when
		boolean result = companyRepository.existsById(companyId);

		// then
		assertFalse(result);
	}
}