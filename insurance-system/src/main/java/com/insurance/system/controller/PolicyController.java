package com.insurance.system.controller;

import com.insurance.system.entity.Policy;
import com.insurance.system.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "http://localhost:3000")
public class PolicyController {

    private final PolicyService policyService;

    @Autowired
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 🟢 Anyone can view policies
    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable Long id) {
        return policyService.getPolicyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔐 Only ADMIN can create policies
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        return new ResponseEntity<>(policyService.createPolicy(policy), HttpStatus.CREATED);
    }

    // 🔐 Only ADMIN can update policies
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Policy> updatePolicy(@PathVariable Long id, @RequestBody Policy policy) {
        return ResponseEntity.ok(policyService.updatePolicy(id, policy));
    }

    // 🔐 Only ADMIN can delete policies
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
