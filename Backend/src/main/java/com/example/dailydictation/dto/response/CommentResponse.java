package com.example.dailydictation.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Integer id;
    private String content;
    private String userName;
    private Integer userId;
    private Integer parentId;
    private Integer courseId;
    private LocalDateTime createDate;
}
