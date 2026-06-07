package com.example.dailydictation.repository;

import com.example.dailydictation.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section,Integer> {
    List<Section> findByTopicId(int topicId);
    Optional<Section> findById(int sectionId);

    @Query("SELECT DISTINCT s FROM Section s JOIN s.courses c WHERE c.level = :level")
    List<Section> findSectionsByCourseLevel(@Param("level") String level);
}
