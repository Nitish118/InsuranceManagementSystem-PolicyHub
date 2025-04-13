package com.insurance.system.service;

import com.insurance.system.entity.Policy;
import com.insurance.system.entity.User;
import com.insurance.system.entity.UserPolicy;
import com.insurance.system.repository.PolicyRepository;
import com.insurance.system.repository.UserPolicyRepository;
import com.insurance.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserPolicyServiceImpl implements UserPolicyService {
    @Autowired
    private UserPolicyRepository userPolicyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PolicyRepository policyRepository;
    
    @Override
    public UserPolicy purchasePolicy(Long userId, Long policyId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Policy> policy = policyRepository.findById(policyId);
        
        if (user.isPresent() && policy.isPresent()) {
            // Check if user already owns this policy
            Optional<UserPolicy> existingPolicies = userPolicyRepository.findByUserAndPolicy(user.get(), policy.get());
            
            if (!existingPolicies.isEmpty()) {
                // User already has this policy - throw exception with message
                throw new IllegalStateException("You have already purchased this policy");
            }
            
            // If we get here, user doesn't have this policy yet
            UserPolicy userPolicy = new UserPolicy();
            userPolicy.setUser(user.get());
            userPolicy.setPolicy(policy.get());
            userPolicy.setPurchaseDate(new Date());
            return userPolicyRepository.save(userPolicy);
        }
        return null;
    }
    
    @Override
    public List<UserPolicy> getUserPolicies(Long userId) {
        return userPolicyRepository.findByUser_UserId(userId);
    }
    
    @Override
    public List<UserPolicy> getAllUserPolicies() {
        return userPolicyRepository.findAll();
    }
}