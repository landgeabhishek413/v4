package com.example.abhihsek.repository;

import com.example.abhihsek.entity.Policy;
import com.example.abhihsek.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<User> findByUser(User user);

    List<Policy> findByUser_Username(String username);
}
