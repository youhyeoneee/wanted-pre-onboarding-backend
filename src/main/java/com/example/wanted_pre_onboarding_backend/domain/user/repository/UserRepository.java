package com.example.wanted_pre_onboarding_backend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wanted_pre_onboarding_backend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
