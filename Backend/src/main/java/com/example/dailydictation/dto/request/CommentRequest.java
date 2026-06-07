package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;

    @Min(value = 1, message = "userId phải lớn hơn 0")
    private int userId;

    @Min(value = 1, message = "courseId phải lớn hơn 0")
    private int courseId;

    // parentId có thể null (comment gốc), nên không bắt buộc validate Min
    private Integer parentId;
}
