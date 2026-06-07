package com.example.dailydictation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Người nhận thông báo (ví dụ: người viết comment)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Người tạo ra thông báo (người react)
    @ManyToOne
    @JoinColumn(name = "trigger_user_id")
    private User triggerUser;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String message;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
