package com.nokia.Nokia.Employee.Knowledge.Copilot.Entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "department")
    private String department;

    @Column(name = "designation")
    private String designation;

    @Column(name = "location")
    private String location;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "manager")
    private String manager;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "certification", columnDefinition = "TEXT")
    private String certification;
}
