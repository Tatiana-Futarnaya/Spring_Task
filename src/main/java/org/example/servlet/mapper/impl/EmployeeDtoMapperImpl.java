package org.example.servlet.mapper.impl;

import org.example.model.Employee;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.example.servlet.mapper.DepartmentDtoMapper;
import org.example.servlet.mapper.PhoneDtoMapper;
import org.example.servlet.mapper.PositionDtoMapper;
import org.example.servlet.mapper.EmployeeDtoMapper;

import java.util.List;

public class EmployeeDtoMapperImpl implements EmployeeDtoMapper {
    private static final PositionDtoMapper positionDtoMapper = PositionDtoMapperImpl.getInstance();
    private static final PhoneDtoMapper phoneDtoMapper = PhoneDtoMapperImpl.getInstance();
    private static final DepartmentDtoMapper departmentDtoMapper = DepartmentDtoMapperImpl.getInstance();
    private static EmployeeDtoMapper instance;

    private EmployeeDtoMapperImpl() {
    }

    public static synchronized EmployeeDtoMapper getInstance() {
        if (instance == null) {
            instance = new EmployeeDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Employee map(EmployeeIncomingDto employeeIncomingDto) {
        return new Employee(
                null,
                employeeIncomingDto.getFirstName(),
                employeeIncomingDto.getLastName(),
                employeeIncomingDto.getPosition(),
                null,
                null
        );
    }

    @Override
    public Employee map(EmployeeUpdateDto employeeUpdateDto) {
        return new Employee(
                employeeUpdateDto.getId(),
                employeeUpdateDto.getFirstName(),
                employeeUpdateDto.getLastName(),
                positionDtoMapper.map(employeeUpdateDto.getPosition()),
                phoneDtoMapper.mapUpdateList(employeeUpdateDto.getPhoneNumberList()),
                departmentDtoMapper.mapUpdateList(employeeUpdateDto.getDepartmentList())
        );
    }

    @Override
    public EmployeeOutGoingDto map(Employee employee) {
        return new EmployeeOutGoingDto(
                employee.getId(),
                employee.getEmployeeFirstName(),
                employee.getEmployeeLastName(),
                positionDtoMapper.map(employee.getPosition()),
                phoneDtoMapper.map(employee.getPhoneNumberList()),
                departmentDtoMapper.map(employee.getDepartmentList())
        );
    }

    @Override
    public List<EmployeeOutGoingDto> map(List<Employee> employeeList) {
        return employeeList.stream().map(this::map).toList();
    }
}
