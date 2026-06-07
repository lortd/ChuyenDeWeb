package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.response.TopicResponse;
import com.example.dailydictation.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicResponse toTopicResponse(Topic topic);

}
