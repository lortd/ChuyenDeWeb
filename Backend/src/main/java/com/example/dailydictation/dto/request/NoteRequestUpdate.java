package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteRequestUpdate {

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Min(value = 1, message = "noteId phải lớn hơn 0")
    private int noteId;
}
