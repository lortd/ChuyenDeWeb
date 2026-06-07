package com.example.dailydictation.service;

import com.example.dailydictation.dto.request.SectionRequest;
import com.example.dailydictation.dto.response.SectionResponse;
import com.example.dailydictation.entity.Section;
import com.example.dailydictation.entity.Topic;
import com.example.dailydictation.mapper.SectionMapper;
import com.example.dailydictation.repository.SectionRepository;
import com.example.dailydictation.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private TopicRepository topicRepository;

    public SectionResponse createSection(SectionRequest sectionRequest) {
        Topic topic = topicRepository.findById(sectionRequest.getTopicId()).orElseThrow(() -> new RuntimeException("not found topic"));
        Section section = Section.builder()
                .countOfCourse(sectionRequest.getCountOfCourse())
                .name(sectionRequest.getName())
                .topic(topic)
                .build();
        return sectionMapper.toSectionResponse(sectionRepository.save(section));
    }

    public List<SectionResponse> showAllSection(int topicId) {
        return sectionRepository.findByTopicId(topicId)
                .stream()
                .map(sectionMapper::toSectionResponse)
                .collect(Collectors.toList());
    }
    public List<SectionResponse> searchLevel (String level){
        return sectionRepository.findSectionsByCourseLevel(level)
                .stream()
                .map(sectionMapper::toSectionResponse)
                .collect(Collectors.toList());

    }

    public void deleteSection(int id) {
        sectionRepository.deleteById(id);
    }

}
