package com.example.dailydictation.controller;

import com.example.dailydictation.dto.response.ApiResponse;
import com.example.dailydictation.dto.response.PracticeResponse;
import com.example.dailydictation.service.OrderService;
import com.example.dailydictation.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/check-order")
    public ApiResponse<Boolean> checkOrder (@RequestParam int userId){
        return ApiResponse.<Boolean>builder()
                .result(orderService.checkOrder(userId))
                .build();
    }
}
