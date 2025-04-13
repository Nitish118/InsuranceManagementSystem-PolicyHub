package com.insurance.system.service;

import com.insurance.system.entity.Policy;
import com.insurance.system.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public Optional<Policy> getPolicyById(Long id) {
        return policyRepository.findById(id);
    }

    @Override
    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    @Override
    public Policy updatePolicy(Long id, Policy updatedPolicy) {
        Optional<Policy> existing = policyRepository.findById(id);
        if (existing.isPresent()) {
            Policy policy = existing.get();
            policy.setPolicyName(updatedPolicy.getPolicyName());
            policy.setDescription(updatedPolicy.getDescription());
            policy.setPremium(updatedPolicy.getPremium());
            return policyRepository.save(policy);
        }
        return null;
    }

    @Override
    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }
}
