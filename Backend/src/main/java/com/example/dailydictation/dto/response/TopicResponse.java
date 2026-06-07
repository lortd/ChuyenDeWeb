package com.example.dailydictation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicResponse {
    private int id;
    private String type;
    private String level;
    private String img;
    private int countTopic;
}
