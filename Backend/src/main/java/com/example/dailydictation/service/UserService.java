package com.example.dailydictation.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.dailydictation.dto.request.UserEditRequest;
import com.example.dailydictation.dto.request.UserRequest;
import com.example.dailydictation.dto.response.UserResponse;
import com.example.dailydictation.dto.response.UserResponseShowNickName;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.ERole;
import com.example.dailydictation.mapper.UserMapper;
import com.example.dailydictation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Cloudinary cloudinary;

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            throw new RuntimeException("User was exist");
        }
        User user = userMapper.toUser(userRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setCreateDate(LocalDateTime.now());
        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.USER);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;

    }

    public UserResponseShowNickName editNickName(int userId, String newNickName) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        user.setNickName(newNickName);
        User newUser = userRepository.save(user);
        UserResponseShowNickName userResponseShowNickName = new UserResponseShowNickName();
        userResponseShowNickName.setNickName(newUser.getNickName());
        return userResponseShowNickName;
    }

    public UserResponse showInformation(int userId) {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        return userMapper.toUserResponse(user);
    }

    public UserResponse editImage(int userId, MultipartFile image) throws IOException {
        User user = userRepository.findUserById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        Map img = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.asMap("resource_type", "auto")
        );

        String newImage = img.get("secure_url").toString();

        user.setImg(newImage);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void changePassword(int userId, String oldPassword, String newPassword) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // So sánh mật khẩu đã mã hóa
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        // Mã hóa mật khẩu mới trước khi lưu
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        userRepository.save(user);
    }

    public void changGmail(int userId, String gmail) {
        User user = userRepository.findUserById(userId).
                orElseThrow(() -> new RuntimeException("User not found"));
        user.setGmail(gmail);
        userRepository.save(user);
    }

    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    public UserResponse editUser(UserEditRequest request) throws IOException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            user.setUserName(request.getUserName());
        }

        if (request.getGmail() != null) {
            user.setGmail(request.getGmail());
        }

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            user.setRoles(Set.of(ERole.valueOf(request.getRole()))); // cập nhật role
        }

        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            String imageUrl = uploadImage(request.getAvatar());
            user.setImg(imageUrl);
        }

        userRepository.save(user);

        return convertToUserResponse(user);
    }

    private String uploadImage(MultipartFile avatar) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNickName(user.getNickName());
        response.setUserName(user.getUserName());
        response.setGmail(user.getGmail());
        response.setImg(user.getImg());
        response.setEnabled(user.isEnabled());

        // Chuyển Set<ERole> sang Set<String>
        Set<String> roleNames = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        response.setRoles(roleNames);

        return response;
    }

}
