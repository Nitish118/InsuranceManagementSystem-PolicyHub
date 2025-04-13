package com.insurance.system.repository;

import com.insurance.system.entity.Policy;
import com.insurance.system.entity.User;
import com.insurance.system.entity.UserPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPolicyRepository extends JpaRepository<UserPolicy, Long> {
    List<UserPolicy> findByUser_UserId(Long userId); // for UserPolicyServiceImpl
    List<UserPolicy> findByUser(User user);           // for ClaimServiceImpl
    Optional<UserPolicy> findByUserAndPolicy(User user, Policy policy); 
}
