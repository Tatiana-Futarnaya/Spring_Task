package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.example.servlet.mapper.EmployeeDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private  EmployeeRepository employeeRepository;
    private EmployeeDtoMapper employeeDtoMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeDtoMapper employeeDtoMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeDtoMapper = employeeDtoMapper;
    }



    @Override
    public EmployeeOutGoingDto save(EmployeeIncomingDto employeeDto) {
        Employee employee = employeeDtoMapper.map(employeeDto);
        employee = employeeRepository.save(employee);
        return employeeDtoMapper.map(employee);
    }

    @Override
    public EmployeeOutGoingDto updateEmployee(EmployeeUpdateDto employeeDto) throws NotFoundException {
        Employee updateEmployee=employeeDtoMapper.map(employeeDto);

        Employee oldEmployee = employeeRepository.findById(updateEmployee.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        updateEmployee.setPosition(oldEmployee.getPosition());
        updateEmployee.setPhoneNumberList(oldEmployee.getPhoneNumberList());
        updateEmployee.setDepartmentList(oldEmployee.getDepartmentList());

        employeeRepository.save(updateEmployee);

        return employeeDtoMapper.map(updateEmployee);
    }

    @Override
    public EmployeeOutGoingDto findById(Long employeeId) throws NotFoundException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));

        return employeeDtoMapper.map(employee);
    }

    @Override
    public List<EmployeeOutGoingDto> findAll() {
        List<Employee> all = employeeRepository.findAll();
        return all.stream()
                .map(employee -> employeeDtoMapper.map(employee))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long positionId) throws NotFoundException {
        employeeRepository.findById(positionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee Not Found"));
        employeeRepository.deleteById(positionId);
    }
}
