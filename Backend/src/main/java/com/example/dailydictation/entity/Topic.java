package com.example.dailydictation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String level;
    private String img;
    private int countTopic;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Section> sections;
}
