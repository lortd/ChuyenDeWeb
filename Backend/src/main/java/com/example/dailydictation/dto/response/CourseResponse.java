package com.example.dailydictation.dto.response;

import com.example.dailydictation.entity.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private int id;
    private String name;
    private String level;
    private short countOfSentence;
    private String mainAudio;
    private List<String> sentences;
    private List<String> sentenceAudios;
    private String transcript;
}
