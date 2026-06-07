package com.example.dailydictation.repository;

import com.example.dailydictation.entity.CommentClosure;
import com.example.dailydictation.entity.CommentClosureId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, CommentClosureId> {
    List<CommentClosure> findAllByIdDescendantId(Integer descendantId);
}
