package org.example.servlet.mapper;

import org.example.model.Employee;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;

import java.util.List;

public interface EmployeeDtoMapper {
    Employee map(EmployeeIncomingDto employeeIncomingDto);

    Employee map(EmployeeUpdateDto employeeUpdateDto);

    EmployeeOutGoingDto map(Employee employee);

    List<EmployeeOutGoingDto> map(List<Employee> employeeList);

}
