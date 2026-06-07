package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String userName; // Tên đăng nhập

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password; // Mật khẩu

}
