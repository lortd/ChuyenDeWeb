package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.AuthenticationRequest;
import com.example.dailydictation.dto.request.IntrospectRequest;
import com.example.dailydictation.dto.request.UserRequestRegister;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.AuthenticationResponse;
import com.example.dailydictation.dto.response.IntrospectResponse;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.ERole;
import com.example.dailydictation.repository.UserRepository;
import com.example.dailydictation.service.AuthenticationService;
import com.example.dailydictation.service.MailService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000") // Cho phép React gọi API

public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;



    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody UserRequestRegister userRequestRegister) {
        String token = UUID.randomUUID().toString();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.USER);
        if (userRepository.existsByUserName(userRequestRegister.getUsername())) {
            throw new RuntimeException("User was exist");
        }
        User user = User.builder()
                .enabled(false)
                .verificationToken(token)
                .userName(userRequestRegister.getUsername())
                .password(passwordEncoder.encode(userRequestRegister.getPassword()))
                .gmail(userRequestRegister.getEmail())
                .roles(roles)
                .nickName(userRequestRegister.getNickName())
                .createDate(LocalDateTime.now())
                .build();
        userRepository.save(user);
        mailService.sendVerificationEmail(user.getGmail(),token);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Token không hợp lệ");
        }
        User user = optionalUser.get();
        System.out.println(user);
        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return ResponseEntity.ok("Tài khoản đã được xác nhận thành công!");
    }


    @PostMapping("/test-log-in")
    public ApiResponse<AuthenticationResponse> testLogin(@RequestBody AuthenticationRequest authenticationRequest) {
        System.out.println("Dữ liệu nhận từ FE: " + authenticationRequest); // In dữ liệu nhận được

        AuthenticationResponse result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> testLogin(@Valid @RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var result = authenticationService.introspect(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @RequestParam String username,
            @RequestParam String email) {

        // Tìm user theo username
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng với tên đăng nhập này.");
        }

        User user = optionalUser.get();

        // Kiểm tra email khớp không
        if (!user.getGmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Email không khớp với tài khoản.");
        }

        // Tạo mật khẩu mặc định mới
        String newPassword = "123456";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Gửi email
        String subject = "Đặt lại mật khẩu - Daily Dictation";
        String body = "Xin chào " + user.getNickName() + ",\n\n" +
                "Bạn đã yêu cầu đặt lại mật khẩu.\n" +
                "Mật khẩu mới của bạn là: " + newPassword + "\n\n" +
                "Vui lòng đăng nhập và đổi lại mật khẩu ngay để đảm bảo an toàn.\n\n" +
                "Trân trọng,\nDaily Dictation Team";

        mailService.sendSimpleEmail(user.getGmail(), subject, body);

        return ApiResponse.<Void>builder()
                .message("Mật khẩu mới đã được gửi đến email của bạn.")
                .build();
    }


}