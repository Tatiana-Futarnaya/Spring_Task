package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;

import java.util.List;

public interface EmployeeService {
    EmployeeOutGoingDto save(EmployeeIncomingDto employeeDto);

    void update(EmployeeUpdateDto employeeDto) throws NotFoundException;

    EmployeeOutGoingDto findById(Long employeeId) throws NotFoundException;

    List<EmployeeOutGoingDto> findAll();

    void delete(Long employeeId) throws NotFoundException;
}
