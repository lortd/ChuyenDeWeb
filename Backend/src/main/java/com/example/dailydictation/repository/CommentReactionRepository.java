package com.example.dailydictation.repository;

import com.example.dailydictation.entity.Comment;
import com.example.dailydictation.entity.CommentReaction;
import com.example.dailydictation.entity.Course;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Integer> {
    List<CommentReaction> findByCourse(Course course);
    CommentReaction findByUserAndCommentAndCourse(User user, Comment comment,Course course);

    void deleteCommentReactionByCommentAndUserAndReaction(Comment comment, User user, Reaction reaction);

    void deleteByUserAndComment(User user, Comment comment);

    boolean existsByCommentIdAndUserId(int commentId, int userId);
}
