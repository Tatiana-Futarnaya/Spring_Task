package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EmployeeService {


    EmployeeOutGoingDto save(EmployeeIncomingDto employeeDto);


    EmployeeOutGoingDto updateEmployee(EmployeeUpdateDto employeeDto) throws NotFoundException;

    EmployeeOutGoingDto findById(Long employeeId) throws NotFoundException;

    List<EmployeeOutGoingDto> findAll();

    void delete(Long employeeId) throws NotFoundException;

}
