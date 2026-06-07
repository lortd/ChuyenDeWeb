package com.example.dailydictation.repository;

import com.example.dailydictation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional< User > findUserById(int id);
    Optional< User > findByUserName(String userName);
    boolean existsByUserName(String userName);
    Optional<User> findByVerificationToken(String token);

    Optional<? extends User> findByGmail(String input);

}
