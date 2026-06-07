package com.example.dailydictation.service;

import com.example.dailydictation.dto.request.FavoriteCourseRequest;
import com.example.dailydictation.dto.response.FavoriteCourseResponse;
import com.example.dailydictation.entity.Course;
import com.example.dailydictation.entity.FavoriteCourse;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.mapper.FavoriteCourseMapper;
import com.example.dailydictation.repository.CourseRepository;
import com.example.dailydictation.repository.FavoriteCourseRepository;
import com.example.dailydictation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteCourseService {

    @Autowired
    private FavoriteCourseRepository favoriteCourseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FavoriteCourseMapper favoriteCourseMapper;

    public void createFavoriteCourse(FavoriteCourseRequest favoriteCourseRequest) {
        User user = userRepository.findUserById(favoriteCourseRequest.getUserId()).orElseThrow(()
                -> new RuntimeException("user not found"));
        Course course = courseRepository.findCourseById(favoriteCourseRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        boolean exists = favoriteCourseRepository.existsByCourseIdAndUserId(favoriteCourseRequest.getUserId(), favoriteCourseRequest.getCourseId());
        if (!exists) {
            FavoriteCourse favoriteCourse = FavoriteCourse.builder()
                    .course(course)
                    .user(user)
                    .build();
            favoriteCourseRepository.save(favoriteCourse);
        }
    }

    public List<FavoriteCourseResponse> showAllFavoriteCourse(int userId) {
        return favoriteCourseRepository.findAllByUserId(userId)
                .stream()
                .map(favoriteCourseMapper::toFavoriteCourseResponse)
                .collect(Collectors.toList());

    }

    public void deleteFavoriteCourse(int userId, int courseId) {
        // Tìm FavoriteCourse theo userId và courseId
        favoriteCourseRepository.findByUserIdAndCourseId(userId, courseId)
                .ifPresent(favoriteCourseRepository::delete);
    }

    public boolean isFavorite(int userId, int courseId) {
        return favoriteCourseRepository.existsByUserIdAndCourseId(userId, courseId);
    }


}
