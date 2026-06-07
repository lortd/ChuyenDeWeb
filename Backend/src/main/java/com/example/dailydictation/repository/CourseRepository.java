package com.example.dailydictation.repository;

import com.example.dailydictation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course getCourseById(int id);

    @Query("SELECT c.sentences FROM Course c WHERE c.Id = :courseId")
    List<String> findSentencesByCourseId(@Param("courseId") int courseId);

    @Query("select c.sentenceAudios from Course c WHERE c.Id=:courseId")
    List<String> findSentenceAudiosByCourseId(@Param("courseId") int courseId);

    Optional<Course> findCourseById(int id);
    @Query("SELECT c.transcript FROM Course c WHERE c.Id = :courseId")
    String getTranscriptById(int courseId);
    @Query("SELECT c.mainAudio FROM Course c WHERE c.Id = :courseId")
    String getMainAudioById (int courseId);

    List<Course>findBySectionId(int sectionId);
}
