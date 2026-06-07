package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.response.NotificationResponse;
import com.example.dailydictation.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mappings({
            @Mapping(source = "user.id", target = "user"),
            @Mapping(source = "course.id", target = "course"),
            @Mapping(source = "course.name", target = "courseName"),
            @Mapping(source = "triggerUser.id", target = "triggerUserId")  // Thêm dòng này
    })
    NotificationResponse toNotificationResponse(Notification notification);
}

