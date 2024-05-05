package org.example.servlet.mapper.impl;

import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.model.Position;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


class EmployeeDtoMapperImplTest {
    private  DepartmentDtoMapper departmentDtoMapper;
    private  PhoneDtoMapper phoneDtoMapper;

    private EmployeeDtoMapper employeeDtoMapper;

    @BeforeEach
    void setUp() {
        departmentDtoMapper=new DepartmentDtoMapperImpl();
        phoneDtoMapper=new PhoneDtoMapperImpl();
        employeeDtoMapper =new EmployeeDtoMapperImpl(departmentDtoMapper,phoneDtoMapper);
    }

    @DisplayName("Employee map(EmployeeIncomingDto")
    @Test
    void mapIncoming() {
        EmployeeIncomingDto dto = new EmployeeIncomingDto(
                "f1",
                "l2",
                new Position(1L, "position 1")
        );
        Employee result = employeeDtoMapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getFirstName(), result.getEmployeeFirstName());
        Assertions.assertEquals(dto.getLastName(), result.getEmployeeLastName());
        Assertions.assertEquals(dto.getPosition().getId(), result.getPosition().getId());
    }

    @DisplayName("Employee map(EmployeeUpdateDto)")
    @Test
    void testMapUpdate() {
        EmployeeUpdateDto dto = new EmployeeUpdateDto(
                100L,
                "f1",
                "l2",
                new PositionUpdateDto(1L, "Position update"),
                List.of(new PhoneUpdateDto()),
                List.of(new DepartmentUpdateDto())
        );
        Employee result = employeeDtoMapper.map(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getFirstName(), result.getEmployeeFirstName());
        Assertions.assertEquals(dto.getLastName(), result.getEmployeeLastName());
        Assertions.assertEquals(dto.getPosition().getId(), result.getPosition().getId());
        Assertions.assertEquals(dto.getPhoneNumberList().size(), result.getPhoneNumberList().size());
        Assertions.assertEquals(dto.getDepartmentList().size(), result.getDepartmentList().size());
    }

    @DisplayName("EmployeeOutGoingDto map(Employee)")
    @Test
    void testMapOutgoing() {
        Employee user = new Employee(
                100L,
                "f1",
                "l2",
                new Position(1L, "Position #1"),
                List.of(new Phone(1L, "1324", null)),
                List.of(new Department(1L, "d1", List.of()))
        );
        EmployeeOutGoingDto result = employeeDtoMapper.map(user);

        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getEmployeeFirstName(), result.getFirstName());
        Assertions.assertEquals(user.getEmployeeLastName(), result.getLastName());
        Assertions.assertEquals(user.getPosition().getId(), result.getPosition().getId());
        Assertions.assertEquals(user.getPhoneNumberList().size(), result.getPhoneNumberList().size());
        Assertions.assertEquals(user.getDepartmentList().size(), result.getDepartmentList().size());
    }

    @DisplayName("List<EmployeeOutGoingDto> map(List<Employee>)")
    @Test
    void testMapList() {
        List<Employee> employeeList = List.of(
                new Employee(
                        100L,
                        "f1",
                        "l2",
                        new Position(1L, "Position #1"),
                        List.of(new Phone(1L, "1324", null)),
                        List.of(new Department(1L, "d1", List.of()))
                ),
                new Employee(
                        101L,
                        "f3",
                        "l4",
                        new Position(1L, "Position #1"),
                        List.of(new Phone(2L, "24242", null)),
                        List.of(new Department(2L, "d2", List.of()))
                )
        );
        int result = employeeDtoMapper.map(employeeList).size();
        Assertions.assertEquals(employeeList.size(), result);
    }
}