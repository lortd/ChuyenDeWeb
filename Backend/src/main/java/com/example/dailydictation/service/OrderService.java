package com.example.dailydictation.service;

import com.example.dailydictation.entity.Order;
import com.example.dailydictation.enums.EStatusVNPay;
import com.example.dailydictation.mapper.OrderRepository;
import com.example.dailydictation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public boolean checkOrder (int userId){
        Order order = orderRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy "));
        if(orderRepository.existsByUserId(userId)){
            if(order.getEStatusVNPay().equals(EStatusVNPay.OPEN)){
                return true;
            }
        }
        return false;
    }
}
