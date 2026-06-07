package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionRequest {

    @NotBlank(message = "Tên section không được để trống")
    private String name;

    @Min(value = 0, message = "Số lượng khóa học phải lớn hơn hoặc bằng 0")
    private int countOfCourse;

    @Min(value = 1, message = "topicId phải lớn hơn 0")
    private int topicId;
}
