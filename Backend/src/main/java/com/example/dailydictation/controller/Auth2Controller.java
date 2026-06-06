package com.example.dailydictation.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
public class Auth2Controller {
    @GetMapping("/success")
    public void success(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) throws IOException {
        // Lấy các thông tin từ OAuth2
        Object idAttr = principal.getAttribute("id");
        if (idAttr == null) {
            idAttr = principal.getAttribute("sub"); // thử thêm trường "sub" nếu là Google
        }
        String id = URLEncoder.encode(String.valueOf(idAttr), StandardCharsets.UTF_8);
        String name = URLEncoder.encode(principal.getAttribute("name"), StandardCharsets.UTF_8);
        String email = URLEncoder.encode(principal.getAttribute("email"), StandardCharsets.UTF_8);
        String picture = URLEncoder.encode(principal.getAttribute("picture"), StandardCharsets.UTF_8);
        String token = URLEncoder.encode((String) principal.getAttribute("token"), StandardCharsets.UTF_8);

        // Tạo URL để redirect về frontend
        String redirectUrl = String.format(
                "http://localhost:3000/oauth-success?id=%s&name=%s&email=%s&picture=%s&token=%s",
                id, name, email, picture, token
        );

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/profile")
    public Object getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return "Not authenticated";
        }
        return principal.getAttributes();
    }
}
