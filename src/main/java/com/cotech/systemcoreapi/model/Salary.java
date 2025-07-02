package com.cotech.systemcoreapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "salary_tbl")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double basicSalary;
    @Column(nullable = false)
    private Double allowance;
    @Column(nullable = false)
    private Double deductions;
    @Column(nullable = false)
    private Double netSalary;
    @Column(nullable = false)
    private LocalDate paymentDate;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Employee> employees;

    @Column(updatable = false)
    private LocalDateTime createAt;
    @Column(insertable = false)
    private LocalDateTime updateAt;
}
