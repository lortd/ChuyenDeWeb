package com.example.dailydictation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteCourseResponse {
    private String courseName;
    private int courseId;
}
