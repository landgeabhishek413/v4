package com.example.abhihsek.service;

import com.example.abhihsek.entity.Claim;
import com.example.abhihsek.entity.Policy;
import com.example.abhihsek.entity.User;
import com.example.abhihsek.repository.ClaimRepository;
import com.example.abhihsek.repository.PolicyRepository;
import com.example.abhihsek.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InsuranceService {
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    public InsuranceService(UserRepository userRepository, PolicyRepository policyRepository,
            ClaimRepository claimRepository) {
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.claimRepository = claimRepository;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAllCustomers() {
        return userRepository.findAll(); // Simplified
    }

    // --- Policy ---
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public List<Policy> getPoliciesForUser(String username) {
        return policyRepository.findByUser_Username(username);
    }

    public void savePolicy(Policy policy) {
        policyRepository.save(policy);
    }

    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }

    // --- Claims ---
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public List<Claim> getClaimsForUser(String username) {
        return claimRepository.findByPolicy_User_Username(username);
    }

    public void saveClaim(Claim claim) {
        claimRepository.save(claim);
    }

    public void deleteClaim(Long id) {
        claimRepository.deleteById(id);
    }
}
