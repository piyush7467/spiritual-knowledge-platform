package com.platform.auth.service;

import com.platform.users.entity.RoleEntity;
import com.platform.users.entity.UserEntity;
import com.platform.users.repository.RoleRepository;
import com.platform.users.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SuperAdminBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SuperAdminBootstrap.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;

    public SuperAdminBootstrap(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${BOOTSTRAP_SUPER_ADMIN_EMAIL:}") String adminEmail,
            @Value("${BOOTSTRAP_SUPER_ADMIN_PASSWORD:}") String adminPassword
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (adminEmail.isBlank() || adminPassword.isBlank() || adminPassword.startsWith("CHANGE_ME")) {
            return;
        }
        if (userRepository.existsByEmailIgnoreCase(adminEmail.trim())) {
            return;
        }
        RoleEntity superAdminRole = roleRepository.findByCode("SUPER_ADMIN").orElse(null);
        if (superAdminRole == null) {
            return;
        }
        UserEntity admin = new UserEntity();
        admin.setEmail(adminEmail.trim().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setDisplayName("Platform Super Admin");
        admin.setPreferredLanguage("en");
        admin.getRoles().add(superAdminRole);
        userRepository.save(admin);
        log.info("Bootstrapped initial SUPER_ADMIN account from environment configuration.");
    }
}

