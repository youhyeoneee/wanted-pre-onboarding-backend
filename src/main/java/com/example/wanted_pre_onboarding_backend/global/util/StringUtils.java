package com.example.wanted_pre_onboarding_backend.global.util;

public class StringUtils {
	public static String toSnakeCase(String input) {
		return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
	}
}
