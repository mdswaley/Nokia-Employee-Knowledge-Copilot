package com.nokia.Nokia.Employee.Knowledge.Copilot.DTO;

import java.time.LocalDate;

public record EmployeeDTO(
        String employeeId,
        String name,
        String email,
        String phone,
        String department,
        String designation,
        String location,
        String skills,
        Integer experienceYears,
        String manager,
        LocalDate joiningDate,
        String employmentType,
        Double salary,
        String certification
) {}
