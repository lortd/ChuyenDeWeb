package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.response.FavoriteCourseResponse;
import com.example.dailydictation.entity.FavoriteCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FavoriteCourseMapper {

    @Mappings({
            @Mapping(source = "course.id", target = "courseId"),
            @Mapping(source = "course.name", target = "courseName")
    })
    FavoriteCourseResponse toFavoriteCourseResponse(FavoriteCourse favoriteCourse);
}

