package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.UserEditRequest;
import com.example.dailydictation.dto.request.UserRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.UserResponse;
import com.example.dailydictation.dto.response.UserResponseShowNickName;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("create-user")
    public ApiResponse<UserResponse> testCreateUser(@Valid @RequestBody UserRequest userRequest) {
        System.out.println("Dữ liệu nhận từ frontend: " + userRequest);

        UserResponse userResponse = userService.createUser(userRequest);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }



    @GetMapping("/get-all-user")
    public ApiResponse<List<User>> getAllUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("userName :" + authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> System.out.println(grantedAuthority.getAuthority()));
        return ApiResponse.<List<User>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @PutMapping("/edit-nick-name-user")
    public ApiResponse<UserResponseShowNickName> editNickName(@RequestParam int userId,
                                                              @RequestParam String newNickName) {
        return ApiResponse.<UserResponseShowNickName>builder()
                .result(userService.editNickName(userId, newNickName))
                .build();
    }

    @GetMapping("/show-information")
    public ApiResponse<UserResponse> showNickName(@RequestParam int userId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("userName :" + authentication.getName());
        return ApiResponse.<UserResponse>builder()
                .result(userService.showInformation(userId))
                .build();
    }

    @PutMapping("/edit-image")
    public ApiResponse<UserResponse> editImg(@RequestParam("userId") int userId,
                                             @RequestParam("newImage") MultipartFile newImage) throws IOException {
        return ApiResponse.<UserResponse>builder()
                .result(userService.editImage(userId, newImage))
                .build();
    }
    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestParam int userId,
                                            @RequestParam String password,
                                            @RequestParam String newPassword) {
        userService.changePassword(userId, password, newPassword);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }
    @PutMapping("/change-gmail")
    public ApiResponse<Void> changeGmail(@RequestParam int userId,
                                         @RequestParam String gmail
    ) {
        userService.changGmail(userId, gmail);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }
    @DeleteMapping("/delete-user")
    public ApiResponse<Void> deleteUser(@RequestParam int userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder()
                .message("User deleted successfully.")
                .build();
    }
    @PutMapping("/edit-user")
    public ApiResponse<UserResponse> editUser(
            @RequestPart("data") UserEditRequest userEditRequest,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws IOException {

        if (avatar != null) {
            userEditRequest.setAvatar(avatar);
        }

        UserResponse updatedUser = userService.editUser(userEditRequest);

        return ApiResponse.<UserResponse>builder()
                .result(updatedUser)
                .build();
    }
}