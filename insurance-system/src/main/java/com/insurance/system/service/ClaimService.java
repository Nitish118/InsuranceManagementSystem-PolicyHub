package com.insurance.system.service;

import com.insurance.system.entity.Claim;

import java.util.List;

public interface ClaimService {
    Claim submitClaim(Long userId, Long policyId, String reason, Double amount);
    List<Claim> getClaimsByUser(Long userId);
    List<Claim> getAllClaims();
    Claim updateClaimStatus(Long claimId, String status);
}
