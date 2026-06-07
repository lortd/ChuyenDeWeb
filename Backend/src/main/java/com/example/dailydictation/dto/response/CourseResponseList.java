package com.example.dailydictation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseList {
    private int id;
    private String name;
    private String level;

    public CourseResponseList(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
