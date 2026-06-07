package com.example.dailydictation.dto.response;
import com.example.dailydictation.entity.Topic;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponse {
    private int id;
    private String name;
    private int countOfCourse;
}
