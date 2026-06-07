package com.example.dailydictation.repository;

import com.example.dailydictation.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
   List<Notification>findAllByUserId(int userId);
}
