package com.example.dailydictation.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.dailydictation.config.CloudinaryConfig;
import com.example.dailydictation.dto.request.TopicRequest;
import com.example.dailydictation.dto.response.TopicResponse;
import com.example.dailydictation.entity.Topic;
import com.example.dailydictation.mapper.TopicMapper;
import com.example.dailydictation.repository.TopicRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private Cloudinary cloudinary;

    // Create Topic
    public TopicResponse createTopic(TopicRequest topicRequest) throws IOException {
        if (cloudinary == null) {
            throw new RuntimeException("Cloudinary object is null!");
        }

        Map<String, Object> image = cloudinary.uploader().upload(
                topicRequest.getImg().getBytes(),
                ObjectUtils.asMap("resource_type", "auto")
        );
        String imageUrl = image.get("secure_url").toString();

        Topic topic = new Topic();
        topic.setType(topicRequest.getType());
        topic.setImg(imageUrl);
        topic.setLevel(topicRequest.getLevel());
        topic.setCountTopic(topicRequest.getCountTopic());

        return topicMapper.toTopicResponse(topicRepository.save(topic));
    }

    // Show all topics
    public List<TopicResponse> showAllTopic() {
        return topicRepository.findAll()
                .stream()
                .map(topicMapper::toTopicResponse)
                .collect(Collectors.toList());
    }

    // Delete topic by ID
    public void deleteTopic(int id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy topic với id: " + id));
        topicRepository.delete(topic);
    }

    public TopicResponse updateTopic(int id, @Valid TopicRequest topicRequest, String imageUrl) throws IOException {
        if (cloudinary == null) {
            throw new RuntimeException("Cloudinary object is null!");
        }

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy topic với id: " + id));

        // Update topic's fields
        topic.setType(topicRequest.getType());
        topic.setLevel(topicRequest.getLevel());
        topic.setCountTopic(topicRequest.getCountTopic());

        // Only update image if a new one is provided
        if (imageUrl != null) {
            topic.setImg(imageUrl);
        }

        // Save and return updated topic
        return topicMapper.toTopicResponse(topicRepository.save(topic));
    }
}
