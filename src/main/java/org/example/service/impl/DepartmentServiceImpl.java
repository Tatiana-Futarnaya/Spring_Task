package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.model.Position;
import org.example.repository.DepartmentRepository;
import org.example.repository.EmployeeRepository;
import org.example.service.DepartmentService;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.DepartmentDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service("DepartmentService")

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository ;
    private final DepartmentDtoMapper departmentDtoMapper;


@Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository,
                                 DepartmentDtoMapper departmentDtoMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentDtoMapper = departmentDtoMapper;
    }



    @Override
    public DepartmentOutGoingDto save(DepartmentIncomingDto departmentDto) {
        Department department = departmentDtoMapper.map(departmentDto);
        department = departmentRepository.save(department);
        return departmentDtoMapper.map(department);
    }

    @Override
    public void update(DepartmentUpdateDto departmentUpdateDto) throws NotFoundException {
        departmentRepository.findById(departmentUpdateDto.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found"));
        Department department=departmentDtoMapper.map(departmentUpdateDto);
        departmentRepository.save(department);

        departmentDtoMapper.map(department);
    }

    @Override
    public DepartmentOutGoingDto findById(Long departmentId) throws NotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found"));

        return departmentDtoMapper.map(department);
    }

    @Override
    public List<DepartmentOutGoingDto> findAll() {
        List<Department> all = departmentRepository.findAll();
        return all.stream()
                .map(department -> departmentDtoMapper.map(department))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long departmentId) throws NotFoundException {
        departmentRepository.findById(departmentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not Found"));
        departmentRepository.deleteById(departmentId);
    }





}
