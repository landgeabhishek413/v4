package com.example.abhihsek.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyNumber;
    private String type; // AUTO, HOME, etc.
    private BigDecimal premium;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    @ManyToOne
    private User user; // The customer who owns this policy
}
