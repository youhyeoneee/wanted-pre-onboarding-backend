package com.example.wanted.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wanted.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
