package com.example.dailydictation.config;

import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.ERole;
import com.example.dailydictation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class ApplicationInitConfig {
    @Autowired
   private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUserName("admin").isEmpty()) {
                var roles = new HashSet<ERole>();
                roles.add(ERole.ADMIN);
                User user = User.builder()
                        .userName("admin")
                        .roles(roles)
                        .password(passwordEncoder.encode("admin"))
                        .enabled(true)
                        .build();

                userRepository.save(user);
            }
        };
    }
}
