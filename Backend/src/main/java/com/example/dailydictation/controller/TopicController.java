package com.example.dailydictation.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.dailydictation.dto.request.TopicRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.TopicResponse;
import com.example.dailydictation.entity.Topic;
import com.example.dailydictation.service.TopicService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TopicController {
    @Autowired
    private TopicService topicService;
    private Cloudinary cloudinary;
    @Autowired
    public TopicController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @PostMapping("/create-topic")
    public ApiResponse<TopicResponse> createTopic(@Valid @ModelAttribute TopicRequest topicRequest) throws IOException {
        // Kiểm tra và xử lý file img từ topicRequest
        MultipartFile imgFile = topicRequest.getImg();
        if (imgFile.isEmpty()) {
            throw new BadRequestException("Ảnh không được để trống");
        }

        TopicResponse topicResponse = topicService.createTopic(topicRequest);
        return ApiResponse.<TopicResponse>builder()
                .result(topicResponse)
                .build();
    }


    @GetMapping("/show-all-topic")
    public ApiResponse<List<TopicResponse>> showAllTopic() {
        List<TopicResponse> topic = topicService.showAllTopic();
        return ApiResponse.<List<TopicResponse>>builder()
                .result(topic)
                .build();
    }

    @DeleteMapping("/delete-topic/{id}")
    public ApiResponse<Void> deleteTopic(@PathVariable("id") int id) {
        topicService.deleteTopic(id);
        return ApiResponse.<Void>builder()
                .message("Xóa topic thành công!")
                .build();
    }

    @PutMapping("/update-topic/{id}")
    public ApiResponse<TopicResponse> updateTopic(@PathVariable("id") int id,
                                                  @Valid @ModelAttribute TopicRequest topicRequest,
                                                  @RequestPart(value = "img", required = false) MultipartFile imgFile) throws IOException {
        String imageUrl = null;

        if (imgFile != null && !imgFile.isEmpty()) {
            imageUrl = uploadImageToCloudinary(imgFile); // Upload image to Cloudinary
        }

        // Update the topic with image URL and other data
        TopicResponse topicResponse = topicService.updateTopic(id, topicRequest, imageUrl);

        return ApiResponse.<TopicResponse>builder()
                .result(topicResponse)
                .build();
    }

    private String uploadImageToCloudinary(MultipartFile imgFile) throws IOException {
        // Upload image to Cloudinary and get the URL
        Map uploadResult = cloudinary.uploader().upload(
                imgFile.getBytes(),
                ObjectUtils.asMap("resource_type", "auto")
        );
        return uploadResult.get("secure_url").toString();  // Retrieve the URL of the uploaded image
    }
}
