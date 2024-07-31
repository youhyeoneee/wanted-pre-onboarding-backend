package com.example.wanted_pre_onboarding_backend.domain.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {
    @Id
    private int id;
    private String username;
    private String pw;
}
