package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.repository.impl.EmployeeRepositoryImpl;
import org.example.service.EmployeeService;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.example.servlet.mapper.EmployeeDtoMapper;
import org.example.servlet.mapper.impl.EmployeeDtoMapperImpl;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository = EmployeeRepositoryImpl.getInstance();
    private final EmployeeDtoMapper employeeDtoMapper = EmployeeDtoMapperImpl.getInstance();
    private static EmployeeService instance;


    private EmployeeServiceImpl() {
    }

    public static synchronized EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeServiceImpl();
        }
        return instance;
    }

    private void checkExistEmployee(Long employeeId) throws NotFoundException {
        if (!employeeRepository.exitsById(employeeId)) {
            throw new NotFoundException("Employee not found.");
        }
    }

    @Override
    public EmployeeOutGoingDto save(EmployeeIncomingDto employeeDto) {
        Employee employee = employeeRepository.save(employeeDtoMapper.map(employeeDto));
        return employeeDtoMapper.map(employeeRepository.findById(employee.getId()).orElse(employee));
    }

    @Override
    public void update(EmployeeUpdateDto employeeDto) throws NotFoundException {
        if (employeeDto == null || employeeDto.getId() == null) {
            throw new IllegalArgumentException();
        }
        checkExistEmployee(employeeDto.getId());
        employeeRepository.update(employeeDtoMapper.map(employeeDto));
    }

    @Override
    public EmployeeOutGoingDto findById(Long employeeId) throws NotFoundException {
        checkExistEmployee(employeeId);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        return employeeDtoMapper.map(employee);
    }

    @Override
    public List<EmployeeOutGoingDto> findAll() {
        List<Employee> all = employeeRepository.findAll();
        return employeeDtoMapper.map(all);
    }

    @Override
    public void delete(Long employeeId) throws NotFoundException {
        checkExistEmployee(employeeId);
        employeeRepository.deleteById(employeeId);
    }
}
