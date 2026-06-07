package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.response.CourseResponse;
import com.example.dailydictation.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseResponse toCourseResponse (Course course);
}
