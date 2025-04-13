package com.insurance.system.service;

import com.insurance.system.entity.Policy;

import java.util.List;
import java.util.Optional;

public interface PolicyService {
    List<Policy> getAllPolicies();
    Optional<Policy> getPolicyById(Long id);
    Policy createPolicy(Policy policy);
    Policy updatePolicy(Long id, Policy policy);
    void deletePolicy(Long id);
}
