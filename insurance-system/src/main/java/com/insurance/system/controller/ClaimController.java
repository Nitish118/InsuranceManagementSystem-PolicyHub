package com.insurance.system.controller;

import com.insurance.system.entity.Claim;
import com.insurance.system.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "http://localhost:3000")
public class ClaimController {
    @Autowired
    private ClaimService claimService;
    
    // Support both request params and request body
    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Claim> submitClaim(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long policyId,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) Double amount,
            @RequestBody(required = false) Map<String, Object> requestBody) {
        
        // Get values from either request params or body
        if (requestBody != null) {
            if (userId == null && requestBody.containsKey("userId")) {
                userId = Long.valueOf(requestBody.get("userId").toString());
            }
            if (policyId == null && requestBody.containsKey("policyId")) {
                policyId = Long.valueOf(requestBody.get("policyId").toString());
            }
            if (reason == null && requestBody.containsKey("reason")) {
                reason = (String) requestBody.get("reason");
            }
            if (amount == null && requestBody.containsKey("amount")) {
                amount = Double.valueOf(requestBody.get("amount").toString());
            }
        }
        
        System.out.println("Received claim from userId=" + userId +
                " policyId=" + policyId + " reason=" + reason + " amount=" + amount);
        
        try {
            Claim claim = claimService.submitClaim(userId, policyId, reason, amount);
            if (claim != null) {
                return new ResponseEntity<>(claim, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.err.println("Exception during claim submission: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Claim>> getClaimsByUser(@PathVariable Long userId) {
        System.out.println("Getting claims for user ID: " + userId);
        List<Claim> claims = claimService.getClaimsByUser(userId);
        System.out.println("Found " + claims.size() + " claims");
        return ResponseEntity.ok(claims);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<?> getAllClaims() {
        // Debug authentication information
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("\n===== DEBUG AUTHENTICATION FOR /api/claims/all =====");
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Name: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        System.out.println("Is authenticated: " + auth.isAuthenticated());
        
        try {
            List<Claim> claims = claimService.getAllClaims();
            System.out.println("Found " + claims.size() + " total claims");
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            System.err.println("Error in getAllClaims controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve claims: " + e.getMessage());
        }
    }
    
    @PutMapping("/{claimId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<Claim> updateClaimStatus(@PathVariable Long claimId,
                                               @RequestParam String status) {
        Claim updated = claimService.updateClaimStatus(claimId, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}