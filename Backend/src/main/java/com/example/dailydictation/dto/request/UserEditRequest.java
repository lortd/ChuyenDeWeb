package com.example.dailydictation.dto.request;

import com.example.dailydictation.enums.ERole;
import jakarta.validation.constraints.*;

import lombok.*;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserEditRequest {

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 20, message = "Username phải từ 4 đến 20 ký tự")
    private String userName;

    @NotBlank(message = "Gmail không được để trống")
    @Email(message = "Gmail không đúng định dạng")
    private String gmail;

    @NotBlank(message = "Role không được để trống")
    private String role;

    private MultipartFile avatar;


}
