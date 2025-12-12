package com.example.abhihsek.repository;

import com.example.abhihsek.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByPolicy_User_Username(String username);
}
