package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.SectionRequest;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.SectionResponse;
import com.example.dailydictation.entity.Section;
import com.example.dailydictation.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SectionController {

    @Autowired
    private SectionService sectionService;
    @PostMapping("/create-section")
    public ApiResponse<SectionResponse> createSection(@Valid @RequestBody SectionRequest sectionRequest){
        SectionResponse sectionResponse = sectionService.createSection(sectionRequest);
        return ApiResponse.<SectionResponse>builder()
                .result(sectionResponse)
                .build();
    }

    @GetMapping("/show-all-section")
    public ApiResponse<List<SectionResponse>> showAllSection (@RequestParam int topicId){
        List<SectionResponse> sectionResponse =sectionService.showAllSection(topicId);
        return ApiResponse.<List<SectionResponse>>builder()
                .result(sectionResponse)
                .build();
    }
    @GetMapping("/search-level")
    public ApiResponse<List<SectionResponse>>searchLevel (@RequestParam String level){
        return ApiResponse.<List<SectionResponse>>builder()
                .result(sectionService.searchLevel(level))
                .build();
    }
    @DeleteMapping("/delete-section/{id}")
    public ApiResponse<Void> deleteSection(@PathVariable("id") int id) {
        sectionService.deleteSection(id);
        return ApiResponse.<Void>builder()
                .message("Xóa section thành công!")
                .build();
    }

}
