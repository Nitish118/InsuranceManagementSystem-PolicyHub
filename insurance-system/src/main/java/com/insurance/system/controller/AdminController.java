// 📁 com.insurance.system.controller.AdminController.java
package com.insurance.system.controller;

import com.insurance.system.entity.Claim;
import com.insurance.system.entity.SupportTicket;
import com.insurance.system.entity.User;
import com.insurance.system.repository.ClaimRepository;
import com.insurance.system.repository.SupportTicketRepository;
import com.insurance.system.repository.UserRepository;
import com.insurance.system.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return "Admin Dashboard Accessed";
    }

    @PutMapping("/users/{userId}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long userId) {
        String loggedInAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User targetUser = userOpt.get();

            if (targetUser.getEmail().equals(loggedInAdminEmail)) {
                return ResponseEntity.badRequest().body("Admins cannot block themselves.");
            }

            if (targetUser.getRole().name().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.badRequest().body("Admins cannot block other admins.");
            }

            targetUser.setBlocked(true);
            userRepository.save(targetUser);
            return ResponseEntity.ok("User blocked");
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User targetUser = userOpt.get();
            targetUser.setBlocked(false);
            userRepository.save(targetUser);
            return ResponseEntity.ok("User unblocked");
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("users", userRepository.count());
        stats.put("policies", policyRepository.count());
        stats.put("claims", claimRepository.count());
        stats.put("tickets", supportTicketRepository.count());

        stats.put("recentUsers", userRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList()));

        stats.put("recentTickets", supportTicketRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(SupportTicket::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList()));

        stats.put("recentClaims", claimRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Claim::getSubmittedAt).reversed())
                .limit(5)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(stats);
    }
    
    
    @GetMapping("/dashboard/recent")
    public ResponseEntity<?> getRecentActivity() {
        Map<String, Object> response = new HashMap<>();
        List<User> recentUsers = userRepository.findTop5ByOrderByCreatedAtDesc();
        List<SupportTicket> recentTickets = supportTicketRepository.findTop5ByOrderByCreatedAtDesc();

        response.put("recentUsers", recentUsers);
        response.put("recentTickets", recentTickets);

        return ResponseEntity.ok(response);
    }

}
