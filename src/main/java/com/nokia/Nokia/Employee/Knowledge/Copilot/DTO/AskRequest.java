package com.nokia.Nokia.Employee.Knowledge.Copilot.DTO;

public record AskRequest(
    String question,
    Long employeeId,
    String employeeName,
    String department,
    String role
    ) {}
