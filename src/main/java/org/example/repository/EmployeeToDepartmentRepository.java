package org.example.repository;

import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.EmployeeToDepartment;

import java.util.List;
import java.util.Optional;

public interface EmployeeToDepartmentRepository extends Repository<EmployeeToDepartment, Long> {
    boolean deleteByEmployeeId(Long employeeId);

    boolean deleteByDepartmentId(Long departmentId);

    List<EmployeeToDepartment> findAllByEmployeeId(Long employeeId);

    List<Department> findDepartmentsByEmployeeId(Long employeeId);

    List<EmployeeToDepartment> findAllByDepartmentId(Long departmentId);

    List<Employee> findEmployeesByDepartmentId(Long departmentId);

    Optional<EmployeeToDepartment> findByEmployeeIdAndDepartmentId(Long employeeId, Long departmentId);
}
