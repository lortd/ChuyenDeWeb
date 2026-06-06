package com.example.dailydictation.entity;

import com.example.dailydictation.enums.Reaction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CommentReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private Course course;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
    }

}
