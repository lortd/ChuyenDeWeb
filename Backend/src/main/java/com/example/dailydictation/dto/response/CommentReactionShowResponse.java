package com.example.dailydictation.dto.response;

import com.example.dailydictation.entity.Comment;
import com.example.dailydictation.enums.Reaction;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReactionShowResponse {
    private Reaction reaction;
    private int commentId;
    private int userId;

}
