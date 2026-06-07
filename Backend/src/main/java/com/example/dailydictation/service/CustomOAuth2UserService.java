package com.example.dailydictation.service;

import com.example.dailydictation.config.Jwt;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private Jwt jwt = new Jwt();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Lấy info người dùng từ Google
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = new User();
        user.setUserName(email);
        user.setRoles(new HashSet<>(List.of(ERole.USER)));
        String token = jwt.generateToken(user);

        // Thêm token vào thông tin người dùng
        Map<String, Object> userAttributes = new HashMap<>(attributes);
        userAttributes.put("token", token);  // Thêm token vào thuộc tính người dùng

        // Có thể lưu vào DB ở đây nếu muốn

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                userAttributes,
                "sub" // hoặc "email" tùy trường bạn muốn làm ID
        );
    }
}
