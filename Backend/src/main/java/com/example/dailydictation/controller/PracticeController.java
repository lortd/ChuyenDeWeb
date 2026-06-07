package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.FavoriteCourseRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.NoteResponse;
import com.example.dailydictation.dto.response.PracticeResponse;
import com.example.dailydictation.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PracticeController {

    @Autowired
    private PracticeService practiceService;
    @GetMapping("/show-practice")
    public ApiResponse<PracticeResponse> showPractice (@RequestParam int userId){
        return ApiResponse.<PracticeResponse>builder()
                .result(practiceService.showPracticeUser(userId))
                .build();
    }
    @PostMapping("/create-practice")
    public ApiResponse<Void> createFavoriteCourse (@RequestParam String finish,@RequestParam int userId,@RequestParam int courseId){
        practiceService.createPractice(finish,userId,courseId);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }

}
