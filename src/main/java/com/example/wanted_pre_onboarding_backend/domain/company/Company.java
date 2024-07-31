package com.example.wanted_pre_onboarding_backend.domain.company;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companys")
public class Company {
    @Id
    private int id;
    private String companyName;
}
