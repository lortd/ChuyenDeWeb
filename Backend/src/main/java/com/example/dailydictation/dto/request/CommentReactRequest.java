package com.example.dailydictation.dto.request;

import com.example.dailydictation.enums.Reaction;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReactRequest {

  @NotNull(message = "userId không được để trống")
  private Integer userId;

  @NotNull(message = "commentId không được để trống")
  private Integer commentId;

  @NotNull(message = "courseId không được để trống")
  private Integer courseId;

  @NotNull(message = "reaction không được để trống")
  private Reaction reaction;
}

