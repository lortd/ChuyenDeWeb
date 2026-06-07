package com.example.dailydictation.service;

import com.example.dailydictation.dto.response.NotificationResponse;
import com.example.dailydictation.entity.Notification;
import com.example.dailydictation.mapper.NotificationMapper;
import com.example.dailydictation.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    public List<NotificationResponse> showAllNotificationUser(int userId) {
        return notificationRepository.findAllByUserId(userId)
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .collect(Collectors.toList());

    }
}
