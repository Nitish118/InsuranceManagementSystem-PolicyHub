package com.insurance.system.repository;

import com.insurance.system.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByIsBlocked(boolean isBlocked);
    List<User> findTop5ByOrderByCreatedAtDesc();
}
