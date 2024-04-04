package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.repository.EmployeeRepository;
import org.example.repository.impl.EmployeeRepositoryImpl;
import org.example.service.EmployeeService;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

class EmployeeServiceImplTest {
    private static EmployeeService employeeService;
    private static EmployeeRepository mockEmployeeRepository;
    private static Position position;
    private static EmployeeRepositoryImpl oldInstance;

    private static void setMock(EmployeeRepository mock) {
        try {
            Field instance = EmployeeRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (EmployeeRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        position = new Position(1L, "position #1");
        mockEmployeeRepository = Mockito.mock(EmployeeRepository.class);
        setMock(mockEmployeeRepository);
        employeeService = EmployeeServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = EmployeeRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockEmployeeRepository);
    }

    @Test
    void save() {
        Long expectedId = 1L;

        EmployeeIncomingDto dto = new EmployeeIncomingDto("f1 name", "l1 name", position);
        Employee employee = new Employee(expectedId, "f1 name", "l1 name", position, List.of(), List.of());

        Mockito.doReturn(employee).when(mockEmployeeRepository).save(Mockito.any(Employee.class));

        EmployeeOutGoingDto result = employeeService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        EmployeeUpdateDto dto = new EmployeeUpdateDto(expectedId, "f1 name", "l1 name",
                new PositionUpdateDto(1L, "position #1"), List.of(), List.of());

        Mockito.doReturn(true).when(mockEmployeeRepository).exitsById(Mockito.any());

        employeeService.update(dto);

        ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);
        Mockito.verify(mockEmployeeRepository).update(argumentCaptor.capture());

        Employee result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        EmployeeUpdateDto dto = new EmployeeUpdateDto(1L, "f1 name", "l1 name", null, null, null);

        Mockito.doReturn(false).when(mockEmployeeRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> employeeService.update(dto) , "Not found."
        );
        Assertions.assertEquals("Employee not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Employee> employee = Optional.of(new Employee(expectedId, "f1 name", "l1 name", position, List.of(), List.of()));

        Mockito.doReturn(true).when(mockEmployeeRepository).exitsById(Mockito.any());
        Mockito.doReturn(employee).when(mockEmployeeRepository).findById(Mockito.anyLong());

        EmployeeOutGoingDto dto = employeeService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Mockito.doReturn(false).when(mockEmployeeRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () ->  employeeService.findById(1L), "Not found."
        );
        Assertions.assertEquals("Employee not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        employeeService.findAll();
        Mockito.verify(mockEmployeeRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 100L;

        Mockito.doReturn(true).when(mockEmployeeRepository).exitsById(Mockito.any());
        employeeService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockEmployeeRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}