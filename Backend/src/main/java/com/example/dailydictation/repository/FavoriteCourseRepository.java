package com.example.dailydictation.repository;

import com.example.dailydictation.entity.FavoriteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteCourseRepository extends JpaRepository<FavoriteCourse,Integer> {
    List<FavoriteCourse>findAllByUserId(int userId);

    Optional<FavoriteCourse> findByUserIdAndCourseId(int userId, int courseId);

    boolean existsByUserIdAndCourseId(int userId, int courseId);

    boolean existsByCourseIdAndUserId(int userId, int courseId);
}
