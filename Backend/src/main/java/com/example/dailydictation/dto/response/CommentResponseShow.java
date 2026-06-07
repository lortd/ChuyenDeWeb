package com.example.dailydictation.dto.response;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseShow {
    private Integer id;
    private String content;
    private String userName;
    private Integer courseId;
    private String courseName;
    private LocalDateTime createDate;

}
