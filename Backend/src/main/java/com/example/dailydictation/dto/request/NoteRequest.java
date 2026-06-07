package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteRequest {

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Min(value = 1, message = "userId phải lớn hơn 0")
    private int userId;
}
