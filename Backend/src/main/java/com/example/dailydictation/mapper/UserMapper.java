package com.example.dailydictation.mapper;

import com.example.dailydictation.dto.request.UserRequest;
import com.example.dailydictation.dto.response.UserResponse;
import com.example.dailydictation.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
}
