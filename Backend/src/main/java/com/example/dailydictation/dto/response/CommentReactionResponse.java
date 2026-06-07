package com.example.dailydictation.dto.response;

import com.example.dailydictation.enums.Reaction;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReactionResponse {
    private int userId;
    private int commentId;
    private int courseId;
    private Reaction reaction;
    private LocalDateTime createDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CommentReactionResponse{" +
                "userId=" + userId +
                ", commentId=" + commentId +
                ", courseId=" + courseId +
                ", reaction=" + reaction +
                ", createDate=" + createDate +
                '}';
    }
}

