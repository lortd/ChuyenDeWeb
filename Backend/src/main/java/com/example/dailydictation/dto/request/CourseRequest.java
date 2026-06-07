package com.example.dailydictation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;

    @NotBlank(message = "Level không được để trống")
    private String level;

    @Min(value = 1, message = "Số lượng câu phải lớn hơn hoặc bằng 1")
    private short countOfSentence;

    @NotNull(message = "File âm thanh chính không được để trống")
    private MultipartFile mainAudio;

    @NotEmpty(message = "Danh sách câu không được để trống")
    private List<@NotBlank(message = "Câu không được để trống") String> sentences;

    // List<MultipartFile> có thể là null hoặc không, không cần phải là @NotNull hoặc @NotEmpty
    private List<MultipartFile> sentenceAudios;

    private String transcript; // Có thể để trống nếu không bắt buộc

    @Min(value = 1, message = "sectionId phải lớn hơn 0")
    private int sectionId;
}
