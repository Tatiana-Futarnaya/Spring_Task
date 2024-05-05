package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Department;
import org.example.repository.DepartmentRepository;
import org.example.repository.EmployeeRepository;
import org.example.servlet.dto.DepartmentIncomingDto;
import org.example.servlet.dto.DepartmentOutGoingDto;
import org.example.servlet.dto.DepartmentUpdateDto;
import org.example.servlet.mapper.DepartmentDtoMapper;
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
class DepartmentServiceImplTest {
    @InjectMocks
    private DepartmentServiceImpl departmentService;
    @Mock
    private  DepartmentRepository mockDepartmentRepository;
    @Mock
    private  EmployeeRepository mockEmployeeRepository;
    @Mock
    private DepartmentDtoMapper departmentDtoMapper;


    @Test
   void save() {
        Long expectedId = 1L;

        DepartmentIncomingDto dto = new DepartmentIncomingDto("department #2");
        Department department = new Department(expectedId, "department #10", List.of());
        DepartmentOutGoingDto departmentOutGoingDto=new DepartmentOutGoingDto(expectedId, "department #10");

        Mockito.when(mockDepartmentRepository.save(Mockito.any(Department.class))).thenReturn(department);
        Mockito.when(departmentDtoMapper.map(dto)).thenReturn(department);
        Mockito.when(departmentDtoMapper.map(department)).thenReturn(departmentOutGoingDto);

        DepartmentOutGoingDto result = departmentService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

   @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        DepartmentUpdateDto dto = new DepartmentUpdateDto(expectedId, "department update #1");
       Department department = new Department(expectedId, "department #10", List.of());
       DepartmentOutGoingDto departmentOutGoingDto=new DepartmentOutGoingDto(expectedId, "department #10");

       Mockito.when(mockDepartmentRepository.save(Mockito.any(Department.class))).thenReturn(department);
       Mockito.when(departmentDtoMapper.map(dto)).thenReturn(department);
       Mockito.when(departmentDtoMapper.map(department)).thenReturn(departmentOutGoingDto);
       Mockito
               .doReturn(Optional.of(department))
               .when(mockDepartmentRepository).findById(department.getId());

        departmentService.update(dto);

        ArgumentCaptor<Department> argumentCaptor = ArgumentCaptor.forClass(Department.class);
        Mockito.verify(mockDepartmentRepository).save(argumentCaptor.capture());

        Department result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        Long expectedId = 1L;

        Department department = new Department(expectedId, "department #10", List.of());
        DepartmentUpdateDto dto = new DepartmentUpdateDto(1L, "department update #1");

        Mockito
                .when(mockDepartmentRepository.findById(department.getId()))
                .thenReturn(Optional.empty());


        ResponseStatusException e  = assertThrows(ResponseStatusException.class,
                () -> departmentService.update(dto));

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Department Not Found\""));
        Mockito.verify(mockDepartmentRepository, Mockito.times(1))
                .findById(department.getId());
    }

   @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Department> department = Optional.of(new Department(expectedId, "department found #1", List.of()));
       DepartmentOutGoingDto departmentOutGoingDto=new DepartmentOutGoingDto(expectedId, "department #10");

       Mockito.doReturn(department).when(mockDepartmentRepository).findById(Mockito.anyLong());
       Mockito.when(departmentDtoMapper.map(department.get())).thenReturn(departmentOutGoingDto);

        DepartmentOutGoingDto dto = departmentService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> departmentService.findById(1L));

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Department Not Found\""));
        Mockito.verify(mockDepartmentRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void findAll() {
        departmentService.findAll();
        Mockito.verify(mockDepartmentRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 1L;
        Department department = new Department(expectedId, "department #10", List.of());

        Mockito
                .doReturn(Optional.of(department))
                .when(mockDepartmentRepository).findById(department.getId());
        departmentService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockDepartmentRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }

}