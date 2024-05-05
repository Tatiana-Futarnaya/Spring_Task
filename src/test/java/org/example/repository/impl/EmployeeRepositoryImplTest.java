package org.example.repository.impl;

import org.example.config.PersistenceConfigForTest;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.model.Position;
import org.example.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.params.*;
import java.util.List;
import java.util.Optional;

import static org.example.config.PersistenceConfigForTest.postgres;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfigForTest.class, EmployeeRepository.class})
class EmployeeRepositoryImplTest {

    @Autowired
    private  EmployeeRepository employeeRepository;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }


    @Test
    void save() {
        String expectedFirstname = "new Firstname";
        String expectedLastname = "new Lastname";

        Employee employee = new Employee(
                null,
                expectedFirstname,
                expectedLastname,
                null,
                null,
                null);
        employee = employeeRepository.save(employee);
        Optional<Employee> resultEmployee = employeeRepository.findById(employee.getId());

        Assertions.assertTrue(resultEmployee.isPresent());
        Assertions.assertEquals(expectedFirstname, resultEmployee.get().getEmployeeFirstName());
        Assertions.assertEquals(expectedLastname, resultEmployee.get().getEmployeeLastName());
    }

    @Test
    void update() {
        String expectedFirstname = "UPDATE Firstname";
        String expectedLastname = "UPDATE Lastname";
        Long expectedPositionId = 1L;

        Employee employeeForUpdate = employeeRepository.findById(3L).get();

        List<Department> departmentList = employeeForUpdate.getDepartmentList();
        int phoneListSize = employeeForUpdate.getPhoneNumberList().size();
        int departmentListSize = employeeForUpdate.getDepartmentList().size();
        Position oldPosition = employeeForUpdate.getPosition();

        Assertions.assertNotEquals(expectedPositionId, employeeForUpdate.getPosition().getId());
        Assertions.assertNotEquals(expectedFirstname, employeeForUpdate.getEmployeeFirstName());
        Assertions.assertNotEquals(expectedLastname, employeeForUpdate.getEmployeeLastName());

        employeeForUpdate.setEmployeeFirstName(expectedFirstname);
        employeeForUpdate.setEmployeeLastName(expectedLastname);
        employeeRepository.save(employeeForUpdate);

        Employee resultEmployee = employeeRepository.findById(3L).get();

        Assertions.assertEquals(expectedFirstname, resultEmployee.getEmployeeFirstName());
        Assertions.assertEquals(expectedLastname, resultEmployee.getEmployeeLastName());

        Assertions.assertEquals(phoneListSize, resultEmployee.getPhoneNumberList().size());
        Assertions.assertEquals(departmentListSize, resultEmployee.getDepartmentList().size());
        Assertions.assertEquals(oldPosition.getId(), resultEmployee.getPosition().getId());

        employeeForUpdate.setPhoneNumberList(List.of());
        employeeForUpdate.setDepartmentList(List.of());
        employeeForUpdate.setPosition(new Position(expectedPositionId, null));
        employeeRepository.save(employeeForUpdate);
        Employee result = employeeRepository.findById(3L).get();

        Assertions.assertEquals(0, result.getPhoneNumberList().size());
        Assertions.assertEquals(0, result.getDepartmentList().size());
        Assertions.assertEquals(expectedPositionId, result.getPosition().getId());

        departmentList.add(new Department(3L, null, null));
        departmentList.add(new Department(4L, null, null));
        employeeForUpdate.setDepartmentList(departmentList);
        employeeRepository.save(employeeForUpdate);
        result = employeeRepository.findById(3L).get();

        Assertions.assertEquals(3, result.getDepartmentList().size());

        departmentList.remove(2);
        employeeForUpdate.setDepartmentList(departmentList);
        employeeRepository.save(employeeForUpdate);
        result = employeeRepository.findById(3L).get();

        Assertions.assertEquals(2, result.getDepartmentList().size());

        employeeForUpdate.setPhoneNumberList(List.of(
                new Phone(null, "new phone", employeeForUpdate),
                new Phone(null, "+1(123)123 245342", employeeForUpdate)));
        employeeForUpdate.setDepartmentList(List.of(new Department(2L, null, null)));

        employeeRepository.save(employeeForUpdate);
        result = employeeRepository.findById(3L).get();

        System.out.println(result.getPhoneNumberList().size());

        Assertions.assertEquals(employeeForUpdate.getPhoneNumberList().size(), result.getPhoneNumberList().size());
        Assertions.assertEquals(1, result.getDepartmentList().size());
    }

    @Test
    void deleteById() {
        int expectedSize = employeeRepository.findAll().size();

        Employee tempeEmployee = new Employee(
                null,
                "Employee for delete Firstname.",
                "Employee for delete Lastname.",
                null,
                null,
                null
        );
        tempeEmployee = employeeRepository.save(tempeEmployee);

        employeeRepository.deleteById(tempeEmployee.getId());
        int positionListAfterSize = employeeRepository.findAll().size();

        Assertions.assertEquals(expectedSize, positionListAfterSize);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "3; true",
            "1; false",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Employee> employee = employeeRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, employee.isPresent());
        employee.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
        employeeRepository.deleteAll();
    }

    @Test
    void findAll() {
        int expectedSize = 7;
        int resultSize = employeeRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

}