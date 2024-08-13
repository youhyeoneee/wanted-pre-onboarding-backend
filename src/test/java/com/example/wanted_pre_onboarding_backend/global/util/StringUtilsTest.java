package com.example.wanted_pre_onboarding_backend.global.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

	@Test
	@DisplayName("카멜 -> 스네이크 케이스 유틸 함수 테스트")
	void toSnakeCase() {
		String camelCase = "userId";

		String result = StringUtils.toSnakeCase(camelCase);

		assertEquals(result, "user_id");
	}
}