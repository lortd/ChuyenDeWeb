package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.request.NoteRequest;
import com.example.dailydictation.dto.response.NoteResponse;
import com.example.dailydictation.entity.Note;
import com.example.dailydictation.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(target = "id", source = "id")

    @Mapping(target = "daysSaved", expression = "java(calculateDaysSaved(note.getCreateDate()))")
    NoteResponse toNoteResponse(Note note);

    default long calculateDaysSaved(LocalDate createdDate) {
        if (createdDate == null) return 0;
        return ChronoUnit.DAYS.between(createdDate, LocalDate.now());
    }
}