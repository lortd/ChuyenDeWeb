package com.example.dailydictation.dto.response;

import com.example.dailydictation.entity.Comment;
import com.example.dailydictation.entity.Course;
import com.example.dailydictation.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private int id;
    private int user;          // user nhận thông báo
    private int course;
    private String message;
    private String courseName;
    private LocalDateTime createdAt;

    private int triggerUserId; // người gây ra thông báo
}

