package com.example.dailydictation.dto.response;

import com.example.dailydictation.enums.EStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeResponse {
    private int score;
    private EStatus status;
    private Long scoreFinish;
}
