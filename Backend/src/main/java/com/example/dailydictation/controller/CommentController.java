package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.CommentRequest;
import com.example.dailydictation.dto.request.CommentRequestUpdate;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.CommentResponse;
import com.example.dailydictation.dto.response.CommentResponseShow;
import com.example.dailydictation.entity.Comment;
import com.example.dailydictation.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public ApiResponse<CommentResponse> comment(@Valid @RequestBody CommentRequest commentRequest) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.comment(commentRequest))
                .build();
    }


    @GetMapping("/get-all-comment")
    public ApiResponse<List<CommentResponse>> getAllComment(@RequestParam int courseId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getAllCommentResponses(courseId))
                .build();
    }


    @PutMapping("/update-comment")
    public ApiResponse<CommentResponse> updateComment(@RequestBody CommentRequestUpdate commentRequestUpdate) {
        CommentResponse commentResponse = commentService.updateComment(commentRequestUpdate);
        return ApiResponse.<CommentResponse>builder()
                .result(commentResponse)
                .build();
    }
    @DeleteMapping("/delete-comment")
    public ApiResponse<Void> deleteComment(@RequestParam int commentId,
                                           @RequestParam int userId) {
        commentService.deleteComment(commentId,userId);
        return ApiResponse.<Void>builder()
                .message("Deleted comment successfully")
                .build();
    }
    @GetMapping("/show-comment-user")
    public ApiResponse<List<CommentResponseShow>> showCommentUser(@RequestParam int userId) {
        return ApiResponse.<List<CommentResponseShow>>builder()
                .result(commentService.showCommentUser(userId))
                .build();
    }
    @GetMapping("/all-comments")
    public ApiResponse<List<CommentResponse>> getAllCommentsInDb() {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getAllComments())
                .build();
    }
}
