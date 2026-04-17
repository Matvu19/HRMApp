package com.hrmapp.api.service;

import com.hrmapp.api.auth.LoginResponse;
import com.hrmapp.api.entity.RoleEntity;
import com.hrmapp.api.entity.UserAccountEntity;
import com.hrmapp.api.entity.UserRoleEntity;
import com.hrmapp.api.repository.RoleRepository;
import com.hrmapp.api.repository.UserAccountRepository;
import com.hrmapp.api.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public LoginResponse login(String username, String password) {
        UserAccountEntity user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"123456".equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        UserRoleEntity userRole = userRoleRepository.findByUserId(user.getUserId()).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"));

        RoleEntity role = roleRepository.findById(userRole.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return new LoginResponse(
                user.getUserId(),
                user.getEmployeeId(),
                user.getUsername(),
                role.getRoleCode(),
                "demo-access-token-" + user.getUsername(),
                "demo-refresh-token-" + user.getUsername()
        );
    }
}