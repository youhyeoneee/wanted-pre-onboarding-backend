package com.example.wanted_pre_onboarding_backend.domain.job;


import com.example.wanted_pre_onboarding_backend.domain.company.Company;
import com.example.wanted_pre_onboarding_backend.domain.job.dto.UpdateJobRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "jobs")
@DynamicInsert
@Getter
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    private String position;
    private int reward;
    private String detail;
    private String skill;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    public Job(Company company, String position, int reward, String detail, String skill) {
        this.company = company;
        this.position = position;
        this.reward = reward;
        this.detail = detail;
        this.skill = skill;
    }

    public void updateJob(UpdateJobRequestDto updateJobRequestDto) {
        this.position = updateJobRequestDto.getPosition();
        this.reward = updateJobRequestDto.getReward();
        this.detail = updateJobRequestDto.getDetail();
        this.skill = updateJobRequestDto.getSkill();
    }
}
