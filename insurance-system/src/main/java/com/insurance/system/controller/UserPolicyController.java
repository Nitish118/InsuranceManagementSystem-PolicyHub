package com.insurance.system.controller;

import com.insurance.system.entity.UserPolicy;
import com.insurance.system.service.UserPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-policies")
@CrossOrigin(origins = "http://localhost:3000")
public class UserPolicyController {
    @Autowired
    private UserPolicyService userPolicyService;
    
    @PostMapping("/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> purchasePolicy(
            @RequestParam Long userId,
            @RequestParam Long policyId) {
        
        // Log the request parameters
        System.out.println("Purchase request - UserId: " + userId + ", PolicyId: " + policyId);
        
        try {
            UserPolicy purchasedPolicy = userPolicyService.purchasePolicy(userId, policyId);
            
            if (purchasedPolicy == null) {
                System.out.println("Purchase failed - user or policy not found");
                return ResponseEntity.badRequest()
                    .body("Failed to purchase policy. Check if user and policy exist.");
            }
            
            System.out.println("Policy purchased successfully with ID: " + purchasedPolicy.getUserPolicyId());
            return ResponseEntity.ok(purchasedPolicy);
        } catch (IllegalStateException e) {
            // This specific exception is thrown when user already has the policy
            System.out.println("Purchase attempt for already owned policy: " + e.getMessage());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            System.err.println("Error in purchase policy: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPolicy>> getUserPolicies(@PathVariable Long userId) {
        List<UserPolicy> policies = userPolicyService.getUserPolicies(userId);
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserPolicy>> getAllUserPolicies() {
        return ResponseEntity.ok(userPolicyService.getAllUserPolicies());
    }
}