package com.nokia.Nokia.Employee.Knowledge.Copilot.Repository;

import com.nokia.Nokia.Employee.Knowledge.Copilot.Entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {
}
