package com.example.dailydictation.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String nickName;
    private String userName;
    private String gmail; // thêm trường này
    private String img;
    private Set<String> roles;
    private boolean enabled; // thêm trường này




}
