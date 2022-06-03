package org.example.querying.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInfo {

    private String firstName;
    private String lastname;
    private LocalDate birthday;

}
