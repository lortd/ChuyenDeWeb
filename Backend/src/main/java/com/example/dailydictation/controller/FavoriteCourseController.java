package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.FavoriteCourseRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.FavoriteCourseResponse;
import com.example.dailydictation.dto.response.PracticeResponse;
import com.example.dailydictation.dto.response.SectionResponse;
import com.example.dailydictation.service.FavoriteCourseService;
import com.example.dailydictation.service.PracticeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FavoriteCourseController {
    @Autowired
    private FavoriteCourseService favoriteCourseService;

    @PostMapping("/create-favorite-course")
    public ApiResponse<Void> createFavoriteCourse(@Valid @RequestBody FavoriteCourseRequest favoriteCourseRequest) {
        favoriteCourseService.createFavoriteCourse(favoriteCourseRequest);
        return ApiResponse.<Void>builder()
                .message("Success")
                .build();
    }


    @GetMapping("/show-all-favorite-course")
    public ApiResponse<List<FavoriteCourseResponse>> showAllFavoriteCourse(@RequestParam int userId){
        return ApiResponse.<List<FavoriteCourseResponse>>builder()
                .result(favoriteCourseService.showAllFavoriteCourse(userId))
                .build();
    }

    // API kiểm tra khóa học đã được yêu thích chưa
    @GetMapping("/check-favorite")
    public Map<String, Boolean> checkFavorite(@RequestParam int userId, @RequestParam int courseId) {
        return Collections.singletonMap("isFavorite", favoriteCourseService.isFavorite(userId, courseId));
    }


    // API xóa khóa học khỏi danh sách yêu thích
    @DeleteMapping("/delete-favorite-course")
    public ApiResponse<Void> deleteFavorite(@RequestParam int userId, @RequestParam int courseId){
        favoriteCourseService.deleteFavoriteCourse(userId, courseId);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }
}

