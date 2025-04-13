package com.insurance.system.service;

import com.insurance.system.entity.UserPolicy;

import java.util.List;

public interface UserPolicyService {
    UserPolicy purchasePolicy(Long userId, Long policyId);
    List<UserPolicy> getUserPolicies(Long userId);
    List<UserPolicy> getAllUserPolicies();
}
