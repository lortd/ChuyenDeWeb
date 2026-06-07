package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicRequest {

    @NotBlank(message = "Type không được để trống")
    private String type;

    @NotBlank(message = "Level không được để trống")
    private String level;

    @NotNull(message = "Ảnh không được để trống")
    private MultipartFile img;

    @Min(value = 1, message = "CountTopic phải lớn hơn 0")
    private int countTopic;
}
