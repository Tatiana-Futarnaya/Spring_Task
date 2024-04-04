package org.example.servlet.mapper.impl;

import org.example.model.Department;
import org.example.servlet.dto.DepartmentIncomingDto;
import org.example.servlet.dto.DepartmentOutGoingDto;
import org.example.servlet.dto.DepartmentUpdateDto;
import org.example.servlet.dto.EmployeeSmallOutGoingDto;
import org.example.servlet.mapper.DepartmentDtoMapper;

import java.util.List;

public class DepartmentDtoMapperImpl implements DepartmentDtoMapper {
    private static DepartmentDtoMapper instance;

    private DepartmentDtoMapperImpl() {
    }

    public static synchronized DepartmentDtoMapper getInstance() {
        if (instance == null) {
            instance = new DepartmentDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Department map(DepartmentIncomingDto dto) {
        return new Department(
                null,
                dto.getName(),
                null
        );
    }

    @Override
    public DepartmentOutGoingDto map(Department department) {
        List<EmployeeSmallOutGoingDto> employeeList = department.getEmployeeList()
                .stream().map(employee -> new EmployeeSmallOutGoingDto(
                        employee.getId(),
                        employee.getEmployeeFirstName(),
                        employee.getEmployeeLastName()
                )).toList();

        return new DepartmentOutGoingDto(
                department.getId(),
                department.getName(),
                employeeList
        );
    }

    @Override
    public Department map(DepartmentUpdateDto updateDto) {
        return new Department(
                updateDto.getId(),
                updateDto.getName(),
                null
        );
    }

    @Override
    public List<DepartmentOutGoingDto> map(List<Department> departmentList) {
        return departmentList.stream().map(this::map).toList();
    }

    @Override
    public List<Department> mapUpdateList(List<DepartmentUpdateDto> departmentList) {
        return departmentList.stream().map(this::map).toList();
    }
}
