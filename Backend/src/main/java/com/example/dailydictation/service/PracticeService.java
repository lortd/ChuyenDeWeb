package com.example.dailydictation.service;

import com.example.dailydictation.dto.response.PracticeResponse;
import com.example.dailydictation.entity.Course;
import com.example.dailydictation.entity.Practice;
import com.example.dailydictation.entity.User;
import com.example.dailydictation.enums.EStatus;
import com.example.dailydictation.repository.CourseRepository;
import com.example.dailydictation.repository.PracticeRepository;
import com.example.dailydictation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PracticeService {

    @Autowired
    private PracticeRepository practiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;


    public PracticeResponse showPracticeUser(int userId) {
        int score = practiceRepository.getTotalScoreByUserId(userId);
        Long scoreFinish = practiceRepository.countByUserId(userId);
        PracticeResponse practiceResponse = PracticeResponse.builder()
                .status(EStatus.finished)
                .score(score)
                .scoreFinish(scoreFinish)
                .build();
        return practiceResponse;
    }

    public void createPractice(String finish, int userId, int courseId) {
        User user = userRepository.findUserById(userId).orElseThrow(()
                -> new RuntimeException("user not found"));
        Course course = courseRepository.findCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (finish.equals("You have completed this exercise, good job!")) {
            boolean exists = practiceRepository.existsByUserIdAndCourseId(userId, courseId);
            if (!exists) {
                Practice practice = Practice.builder()
                        .user(user)
                        .status(EStatus.finished)
                        .course(course)
                        .score(100)
                        .build();
                practiceRepository.save(practice);
            }
        }
    }
}