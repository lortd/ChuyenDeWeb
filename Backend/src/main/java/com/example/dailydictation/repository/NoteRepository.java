package com.example.dailydictation.repository;

import com.example.dailydictation.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findAllByUserId(int userId);

    void deleteById(int noteId);

    Optional<Note> findByIdAndUserId(int noteId, int userId);
}
