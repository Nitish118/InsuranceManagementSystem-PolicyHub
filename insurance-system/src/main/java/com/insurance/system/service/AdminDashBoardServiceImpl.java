package com.insurance.system.service;

import com.insurance.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminDashBoardServiceImpl implements AdminDashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public long getTotalPolicies() {
        return policyRepository.count();
    }

    @Override
    public long getTotalClaims() {
        return claimRepository.count();
    }

    @Override
    public long getTotalTickets() {
        return supportTicketRepository.count();
    }
}
