package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.SectionRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.NotificationResponse;
import com.example.dailydictation.dto.response.SectionResponse;
import com.example.dailydictation.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {


    @Autowired
    private NotificationService notificationService;

    @GetMapping("/show-all-notification")
    public ApiResponse<List<NotificationResponse>>  showAllNotificationUser(@RequestParam int userId){
        List<NotificationResponse> notificationResponse = notificationService.showAllNotificationUser(userId);
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationResponse)
                .build();
    }
}
