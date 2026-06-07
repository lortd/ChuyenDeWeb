package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.request.CommentReactRequest;
import com.example.dailydictation.dto.response.CommentReactionResponse;
import com.example.dailydictation.entity.CommentReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentReactionMapper {
    CommentReactionResponse toCommentReaction (CommentReaction  commentReaction);
}
