package com.insurance.system.service;

import com.insurance.system.entity.Claim;
import com.insurance.system.entity.User;
import com.insurance.system.entity.Policy;
import com.insurance.system.entity.UserPolicy;
import com.insurance.system.repository.ClaimRepository;
import com.insurance.system.repository.PolicyRepository;
import com.insurance.system.repository.UserPolicyRepository;
import com.insurance.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimServiceImpl implements ClaimService {
    @Autowired
    private ClaimRepository claimRepository;
    
    @Autowired
    private UserPolicyRepository userPolicyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PolicyRepository policyRepository;
    
    @Override
    public Claim submitClaim(Long userId, Long policyId, String reason, Double amount) {
        System.out.println("Processing claim submission - UserId: " + userId + ", PolicyId: " + policyId);
        
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            System.out.println("User not found with ID: " + userId);
            return null;
        }
        
        Optional<Policy> policy = policyRepository.findById(policyId);
        if (!policy.isPresent()) {
            System.out.println("Policy not found with ID: " + policyId);
            return null;
        }
        
        Optional<UserPolicy> userPolicy = userPolicyRepository.findByUserAndPolicy(user.get(), policy.get());
        if (!userPolicy.isPresent()) {
            System.out.println("UserPolicy not found for User: " + userId + " and Policy: " + policyId);
            return null;
        }
        
        try {
            Claim claim = new Claim();
            claim.setUserPolicy(userPolicy.get());
            claim.setClaimReason(reason);
            claim.setClaimAmount(amount);
            claim.setClaimStatus("SUBMITTED");
            claim.setSubmittedAt(new Date());
            
            System.out.println("Saving claim for userPolicy: " + userPolicy.get().getUserPolicyId());
            return claimRepository.save(claim);
        } catch (Exception e) {
            System.err.println("Error saving claim: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Claim> getClaimsByUser(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                List<UserPolicy> userPolicies = userPolicyRepository.findByUser(user.get());
                return claimRepository.findByUserPolicyIn(userPolicies);
            }
            return List.of();
        } catch (Exception e) {
            System.err.println("Error getting claims for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Claim> getAllClaims() {
        try {
            System.out.println("Fetching all claims (with valid UserPolicy)...");

            List<Claim> allClaims = claimRepository.findAll();

            List<Claim> validClaims = allClaims.stream()
                .filter(claim -> {
                    try {
                        UserPolicy userPolicy = claim.getUserPolicy();
                        return userPolicy != null &&
                               userPolicy.getUser() != null &&
                               userPolicy.getPolicy() != null;
                    } catch (Exception e) {
                        System.err.println("Invalid claim found (skipped): " + e.getMessage());
                        return false;
                    }
                })
                .toList();

            System.out.println("Valid claims after filtering: " + validClaims.size());
            return validClaims;

        } catch (Exception e) {
            System.err.println("Error fetching claims: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    
    @Override
    public Claim updateClaimStatus(Long claimId, String status) {
        try {
            Optional<Claim> claimOpt = claimRepository.findById(claimId);
            if (claimOpt.isPresent()) {
                Claim claim = claimOpt.get();
                claim.setClaimStatus(status.toUpperCase());
                claim.setReviewedAt(new Date());
                return claimRepository.save(claim);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error updating claim status: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}