package com.hrmapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long employeeId;

    private String username;

    private String passwordHash;

    private String authProvider;

    private Boolean mfaEnabled;

    private Boolean biometricEnabled;

    private String status;
}