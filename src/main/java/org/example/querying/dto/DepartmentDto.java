package org.example.querying.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentDto {
    private String name;
    private Double amount;

    public DepartmentDto(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }
}
