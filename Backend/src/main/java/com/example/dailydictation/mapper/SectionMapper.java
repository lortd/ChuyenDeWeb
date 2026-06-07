package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.response.SectionResponse;
import com.example.dailydictation.entity.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    SectionResponse toSectionResponse(Section section);
}
