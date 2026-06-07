package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCourseRequest {

    @Min(value = 1, message = "courseId phải lớn hơn 0")
    private int courseId;

    @Min(value = 1, message = "userId phải lớn hơn 0")
    private int userId;
}
