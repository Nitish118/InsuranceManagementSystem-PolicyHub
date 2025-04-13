package com.insurance.system.repository;

import com.insurance.system.entity.Claim;
import com.insurance.system.entity.UserPolicy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByUserPolicy_User_UserId(Long userId);
    List<Claim> findByUserPolicyIn(List<UserPolicy> userPolicies);
    

}
