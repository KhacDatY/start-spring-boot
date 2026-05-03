package com.khac_dat.identity_service.config;

import com.khac_dat.identity_service.entity.Role;
import com.khac_dat.identity_service.entity.User;
import com.khac_dat.identity_service.enums.RoleName;
import com.khac_dat.identity_service.exception.AppException;
import com.khac_dat.identity_service.exception.ErrorCode;
import com.khac_dat.identity_service.repository.RoleRepository;
import com.khac_dat.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args ->{
            if(userRepository.findByEmail("admin@company.com").isEmpty()){

                Role adminRole = roleRepository.findByName(RoleName.SUPER_ADMIN.name())
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name(RoleName.SUPER_ADMIN.name())
                                .build()));

                Role employeeRole = roleRepository.findByName(RoleName.EMPLOYEE.name())
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name(RoleName.EMPLOYEE.name())
                                .build()));
                var roles = new HashSet<Role>();
                roles.add(adminRole);
                roles.add(employeeRole);
                User user = User.builder()
                        .email("admin@company.com")
                        .password(passwordEncoder.encode("admin"))
                        .fullName("Super Administrator")
                        .enabled(true)
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("Tai khoan admin da duoc tao, email: admin@company.com");
            }
        };
    }

}
