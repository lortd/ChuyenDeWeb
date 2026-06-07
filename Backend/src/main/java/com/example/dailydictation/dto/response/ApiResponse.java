package com.example.dailydictation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse <T>{
    @Builder.Default
    private int code = 200;
    private String message;
    private T result;
}
