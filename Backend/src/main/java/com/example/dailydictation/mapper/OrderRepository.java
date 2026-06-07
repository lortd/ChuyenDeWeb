package com.example.dailydictation.mapper;

import com.example.dailydictation.entity.Order;
import com.example.dailydictation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    boolean existsByUserId(int userId);


    Optional<Order> findByUserId(int userId);
}
