package com.example.abhihsek.config;

import com.example.abhihsek.entity.Policy;
import com.example.abhihsek.entity.User;
import com.example.abhihsek.service.InsuranceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(InsuranceService service) {
        return args -> {
            // Create Agent
            User agent = new User();
            agent.setName("Agent Smith");
            agent.setUsername("agent");
            agent.setPassword("password");
            agent.setRole("AGENT");
            service.saveUser(agent);

            // Create Customer
            User customer = new User();
            customer.setName("John Doe");
            customer.setUsername("user");
            customer.setPassword("password");
            customer.setRole("CUSTOMER");
            service.saveUser(customer);

            // Create Policy for Customer
            Policy p1 = new Policy();
            p1.setPolicyNumber("POL-001");
            p1.setType("AUTO");
            p1.setPremium(new BigDecimal("1200.00"));
            p1.setStatus("ACTIVE");
            p1.setStartDate(LocalDate.now());
            p1.setEndDate(LocalDate.now().plusYears(1));
            p1.setUser(customer);
            service.savePolicy(p1);

            System.out.println("Dummy data loaded: agent/password, user/password");
        };
    }
}
