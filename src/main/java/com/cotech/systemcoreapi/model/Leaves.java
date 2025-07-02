package com.cotech.systemcoreapi.model;

import com.cotech.systemcoreapi.model.Enum.LeavesStatus;
import com.cotech.systemcoreapi.model.Enum.LeavesType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "leaves_tbl")
public class Leaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeavesType leavesType;
    @Enumerated(EnumType.STRING)
    private LeavesStatus leavesStatus;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    @Column(insertable = false)
    private LocalDateTime updatedAt;
}
