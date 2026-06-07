package com.example.dailydictation.controller;

import com.example.dailydictation.dto.request.NoteRequest;
import com.example.dailydictation.dto.request.NoteRequestUpdate;
import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.NoteResponse;
import com.example.dailydictation.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping("/create-note")
    public ApiResponse<NoteResponse> createNote(@Valid @RequestBody NoteRequest noteRequest) {
        return ApiResponse.<NoteResponse>builder()
                .result(noteService.createNote(noteRequest))
                .build();
    }

    @GetMapping("/show-all-note")
    public ApiResponse<List<NoteResponse>> showAllNote(@RequestParam int userId) {
        return ApiResponse.<List<NoteResponse>>builder()
                .result(noteService.showAllNote(userId))
                .build();
    }

    @PutMapping("/update-note")
    public ApiResponse<NoteResponse> updateNote(@Valid @RequestBody NoteRequestUpdate noteRequestUpdate) {
        return ApiResponse.<NoteResponse>builder()
                .result(noteService.updateNote(noteRequestUpdate))
                .build();
    }


    @DeleteMapping("/delete-note")
    public ApiResponse<Void> deleteNote(@RequestParam int noteId) {
        noteService.deleteNote(noteId);
        return ApiResponse.<Void>builder()
                .message("delete success")
                .build();
    }
}