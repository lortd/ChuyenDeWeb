package com.example.dailydictation.repository;

import com.example.dailydictation.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice,Integer> {
    @Query("SELECT SUM(p.score) FROM Practice p WHERE p.user.Id = :userId")
    Integer getTotalScoreByUserId(Integer userId);

    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);

    Long countByUserId(int userId);
}
