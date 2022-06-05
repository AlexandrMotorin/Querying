package org.example.querying.model;

import lombok.*;

import jakarta.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private EmployeeInfo employeeInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Department department;

}

