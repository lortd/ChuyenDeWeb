package com.example.dailydictation.repository;

import com.example.dailydictation.dto.response.CommentResponse;
import com.example.dailydictation.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByCourseId(int courseId);

    Optional<Comment> findCommentById(int parentId);

    Comment findByIdAndUserId(int commentId,int userId);

    void deleteByIdAndUserId(int commentId,int userId);

    List<Comment>findAllByUserId (int userId);


}
