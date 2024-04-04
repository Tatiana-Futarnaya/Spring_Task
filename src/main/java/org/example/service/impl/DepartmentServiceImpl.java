package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Department;
import org.example.model.EmployeeToDepartment;
import org.example.repository.DepartmentRepository;
import org.example.repository.EmployeeRepository;
import org.example.repository.EmployeeToDepartmentRepository;
import org.example.repository.impl.DepartmentRepositoryImpl;
import org.example.repository.impl.EmployeeRepositoryImpl;
import org.example.repository.impl.EmployeeToDepartmentRepositoryImpl;
import org.example.service.DepartmentService;
import org.example.servlet.dto.DepartmentIncomingDto;
import org.example.servlet.dto.DepartmentOutGoingDto;
import org.example.servlet.dto.DepartmentUpdateDto;
import org.example.servlet.mapper.DepartmentDtoMapper;
import org.example.servlet.mapper.impl.DepartmentDtoMapperImpl;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository = DepartmentRepositoryImpl.getInstance();
    private final EmployeeRepository employeeRepository = EmployeeRepositoryImpl.getInstance();
    private final EmployeeToDepartmentRepository employeeToDepartmentRepository = EmployeeToDepartmentRepositoryImpl.getInstance();
    private static final DepartmentDtoMapper departmentDtoMapper = DepartmentDtoMapperImpl.getInstance();
    private static DepartmentService instance;


    private DepartmentServiceImpl() {
    }

    public static synchronized DepartmentService getInstance() {
        if (instance == null) {
            instance = new DepartmentServiceImpl();
        }
        return instance;
    }

    private void checkExistDepartment(Long departmentId) throws NotFoundException {
        if (!departmentRepository.exitsById(departmentId)) {
            throw new NotFoundException("Department not found.");
        }
    }

    @Override
    public DepartmentOutGoingDto save(DepartmentIncomingDto departmentDto) {
        Department department = departmentDtoMapper.map(departmentDto);
        department = departmentRepository.save(department);
        return departmentDtoMapper.map(department);
    }

    @Override
    public void update(DepartmentUpdateDto departmentUpdateDto) throws NotFoundException {
        checkExistDepartment(departmentUpdateDto.getId());
        Department department = departmentDtoMapper.map(departmentUpdateDto);
        departmentRepository.update(department);
    }

    @Override
    public DepartmentOutGoingDto findById(Long departmentId) throws NotFoundException {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() ->
                new NotFoundException("Department not found."));
        return departmentDtoMapper.map(department);
    }

    @Override
    public List<DepartmentOutGoingDto> findAll() {
        List<Department> departmentList = departmentRepository.findAll();
        return departmentDtoMapper.map(departmentList);
    }

    @Override
    public void delete(Long departmentId) throws NotFoundException {
        checkExistDepartment(departmentId);
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public void deleteEmployeeFromDepartment(Long departmentId, Long employeeId) throws NotFoundException {
        checkExistDepartment(departmentId);
        if (employeeRepository.exitsById(employeeId)) {
            EmployeeToDepartment employeeToDepartment = employeeToDepartmentRepository.findByEmployeeIdAndDepartmentId(employeeId, departmentId)
                    .orElseThrow(() -> new NotFoundException("Link many to many Not found."));

            employeeToDepartmentRepository.deleteById(employeeToDepartment.getId());
        } else {
            throw new NotFoundException("Employee not found.");
        }

    }

    @Override
    public void addEmployeeToDepartment(Long departmentId, Long employeeId) throws NotFoundException {
        checkExistDepartment(departmentId);
        if (employeeRepository.exitsById(employeeId)) {
            EmployeeToDepartment employeeToDepartment = new EmployeeToDepartment(
                    null,
                    employeeId,
                    departmentId
            );
            employeeToDepartmentRepository.save(employeeToDepartment);
        } else {
            throw new NotFoundException("Employee not found.");
        }

    }



}
