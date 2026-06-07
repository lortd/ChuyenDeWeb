package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.CourseRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.CourseResponse;
import com.example.dailydictation.dto.response.CourseResponseList;
import com.example.dailydictation.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // API tạo khóa học mới
    @PostMapping(value = "/create-course")
    public ApiResponse<Void> createCourse(@Valid @ModelAttribute CourseRequest courseRequest, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validate, trả lỗi về client
            String errors = bindingResult.getAllErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ApiResponse.<Void>builder()
                    .message("Validation failed: " + errors)
                    .build();
        }

        // Tạo khóa học thông qua service
        courseService.createCourse(courseRequest);

        return ApiResponse.<Void>builder()
                .message("Course created successfully!")
                .build();
    }

    // API tải file âm thanh
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String url) throws IOException {
        byte[] fileData = courseService.downloadFile(url);
        if (fileData == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Trích xuất tên file từ URL (vd: https://abc.com/audio.mp3 -> audio.mp3)
        String fileName = extractFileName(url);
        // Đặt kiểu nội dung file trả về là audio/mpeg (cho file mp3)
        String contentType = "audio/mpeg"; // Có thể đoán theo đuôi file hoặc tùy chỉnh động

        // Tạo header cho phản hồi HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));

        // Đặt Content-Disposition để trình duyệt hiểu rằng đây là file để tải xuống
        headers.setContentDisposition(ContentDisposition
                .attachment() // Đánh dấu là file đính kèm (tải về)
                .filename(UriUtils.encode(fileName, StandardCharsets.UTF_8))
                .build());

        // Trả về dữ liệu file, kèm header và mã HTTP 200 (OK)
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    // Phương thức phụ dùng để trích xuất tên file từ URL
    private String extractFileName(String fileUrl) {
        try {
            // Lấy phần path từ URL (vd: /music/audio.mp3)
            String path = new URL(fileUrl).getPath();
            // Lấy phần cuối cùng sau dấu "/" (vd: audio.mp3)
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            // Nếu có lỗi (URL sai,...), trả tên mặc định
            return "audio.mp3";
        }
    }

    // API lấy thông tin chi tiết về một khóa học
    @GetMapping("/get-course")
    public ApiResponse<CourseResponse> getCourse(@RequestParam int courseId) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.getCourse(courseId))
                .build();
    }

    // API lấy danh sách các câu trong khóa học
    @GetMapping("/get-list-sentence")
    public List<String> getListSentence(@RequestParam int courseId) {
        return courseService.getListSentence(courseId);
    }

    // API kiểm tra câu người dùng nhập vào có đúng không
    @PostMapping("/check-sentence")
    public String checkSentence(@RequestParam int courseId, @RequestParam int userId ,@RequestBody String userSentence) {
        return courseService.checkSentence(courseId, userSentence, userId);
    }

    // API lấy âm thanh của một câu
    @GetMapping("/get-audio-sentence")
    public String getAudioSentence(@RequestParam int courseId) {
        return courseService.getAudioSentence(courseId);
    }

    // API lấy transcript của khóa học
    @GetMapping("/get-transcript")
    public String getTranscript(@RequestParam int courseId) {
        return courseService.getTranscript(courseId);
    }

    // API lấy âm thanh chính của khóa học
    @GetMapping("/get-main-audio")
    public String getMainAudio(@RequestParam int courseId) {
        return courseService.getMainAudio(courseId);
    }

    // API hiển thị tất cả các khóa học trong một section
    @GetMapping("/show-all-course")
    public ApiResponse<List<CourseResponseList>> showAllCourse(@RequestParam int sectionId) {
        List<CourseResponseList> courseResponseList = courseService.showAllCourse(sectionId);
        return ApiResponse.<List<CourseResponseList>>builder()
                .result(courseResponseList)
                .build();
    }

    // API lấy tất cả tên khóa học
    @GetMapping("/all-course-names")
    public ApiResponse<List<CourseResponseList>> getAllCourseNames() {
        List<CourseResponseList> courseList = courseService.getAllCourseNames();
        return ApiResponse.<List<CourseResponseList>>builder()
                .result(courseList)
                .build();
    }
    @DeleteMapping("/delete-course")
    public ApiResponse<Void> deleteCourse(@RequestParam int courseId) {
        try {
            // Gọi service để xóa khóa học theo ID
            courseService.deleteCourse(courseId);

            return ApiResponse.<Void>builder()
                    .message("Course deleted successfully!")
                    .build();
        } catch (Exception e) {
            // Nếu có lỗi xảy ra, trả về thông báo lỗi
            return ApiResponse.<Void>builder()
                    .message("Error deleting course: " + e.getMessage())
                    .build();
        }
}
}
