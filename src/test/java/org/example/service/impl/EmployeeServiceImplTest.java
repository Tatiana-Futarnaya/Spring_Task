package org.example.service.impl;


import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.repository.EmployeeRepository;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.EmployeeDtoMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @InjectMocks
    private  EmployeeServiceImpl employeeService;
    @Mock
    private  EmployeeRepository mockEmployeeRepository;
    @Mock
    private EmployeeDtoMapper employeeDtoMapper;
    @Mock
    private Position position;


    @Test
    void save() {
        Long expectedId = 1L;

        final EmployeeIncomingDto dto = new EmployeeIncomingDto("f1 name", "l1 name", position);
        final Employee employee = new Employee(expectedId, "f1 name", "l1 name", position, List.of(), List.of());
        final PositionOutGoingDto position=new PositionOutGoingDto();
        final EmployeeOutGoingDto employeeOutGoingDto=new EmployeeOutGoingDto(expectedId, "f1 name", "l1 name",
                position , List.of(),List.of() );

        Mockito.when(mockEmployeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        Mockito.when(employeeDtoMapper.map(dto)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.map(employee)).thenReturn(employeeOutGoingDto);

        EmployeeOutGoingDto result = employeeService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

       Employee employee = new Employee(expectedId, "f1 name", "l1 name",
               position, List.of(), List.of());

        EmployeeUpdateDto dto = new EmployeeUpdateDto(null, "f1 name", "l1 name",
                new PositionUpdateDto(1L, "position #1"), List.of(), List.of());
        PositionOutGoingDto position=new PositionOutGoingDto();
        EmployeeOutGoingDto employeeOutGoingDto=new EmployeeOutGoingDto(expectedId, "f1 name", "l1 name",
                position , List.of(),List.of() );

        Mockito.when(mockEmployeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        Mockito.when(employeeDtoMapper.map(dto)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.map(employee)).thenReturn(employeeOutGoingDto);

        Mockito
                .doReturn(Optional.of(employee))
                .when(mockEmployeeRepository).findById(employee.getId());

        employeeService.updateEmployee(dto);

        ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);
        Mockito.verify(mockEmployeeRepository).save(argumentCaptor.capture());

        Employee result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        Long expectedId = 1L;

        Employee employee = new Employee(expectedId, "f1 name", "l1 name",
                position, List.of(), List.of());
        EmployeeUpdateDto dto = new EmployeeUpdateDto(1L, "f1 name", "l1 name", null, null, null);

        Mockito
                .when(mockEmployeeRepository.findById(employee.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(employeeDtoMapper.map(dto)).thenReturn(employee);

        ResponseStatusException e  = assertThrows(ResponseStatusException.class,
                () -> employeeService.updateEmployee(dto));

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Employee Not Found\""));
        Mockito.verify(mockEmployeeRepository, Mockito.times(1))
                .findById(employee.getId());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Employee> employee = Optional.of(new Employee(expectedId, "f1 name", "l1 name", position, List.of(), List.of()));
        PositionOutGoingDto position=new PositionOutGoingDto();
        EmployeeOutGoingDto employeeOutGoingDto=new EmployeeOutGoingDto(employee.get().getId(), "f1 name", "l1 name",
                position , List.of(),List.of() );


        Mockito.doReturn(employee).when(mockEmployeeRepository).findById(Mockito.anyLong());
        Mockito.when(employeeDtoMapper.map(employee.get())).thenReturn(employeeOutGoingDto);

        EmployeeOutGoingDto dto = employeeService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
        Mockito.verify(mockEmployeeRepository, Mockito.times(1))
                .findById(expectedId);
    }

    @Test
    void findByIdNotFound() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () ->  employeeService.findById(1L) );

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Employee Not Found\""));
        Mockito.verify(mockEmployeeRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void findAll() {
        employeeService.findAll();
        Mockito.verify(mockEmployeeRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 1L;

        Employee employee = new Employee(expectedId, "f1 name", "l1 name",
                position, List.of(), List.of());

        Mockito
                .doReturn(Optional.of(employee))
                .when(mockEmployeeRepository).findById(employee.getId());

        employeeService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockEmployeeRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}