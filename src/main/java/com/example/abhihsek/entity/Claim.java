package com.example.abhihsek.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claimNumber;
    private String description;
    private LocalDate claimDate;
    private BigDecimal amount;
    private String status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    private Policy policy;
}
