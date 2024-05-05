package org.example.repository.impl;

import org.example.config.PersistenceConfigForTest;
import org.example.model.Department;
import org.example.repository.DepartmentRepository;
import org.example.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import static org.example.config.PersistenceConfigForTest.postgres;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfigForTest.class, EmployeeRepository.class, DepartmentRepository.class})
class DepartmentRepositoryImplTest {

    @Autowired
    public  DepartmentRepository departmentRepository;

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
        String expectedName = "new Department Yo!";
        Department department = new Department(
                null,
                expectedName,
                null
        );
        department = departmentRepository.save(department);
        Optional<Department> resultDepartment = departmentRepository.findById(department.getId());

        Assertions.assertTrue(resultDepartment.isPresent());
        Assertions.assertEquals(expectedName, resultDepartment.get().getName());

    }

    @Test
    void update() {
        String expectedName = "Update department name";

        Department department = departmentRepository.findById(2L).get();
        String oldName = department.getName();
        int expectedSizeEmployeeList = department.getEmployeeList().size();
        department.setName(expectedName);
        departmentRepository.save(department);

        Department resultDepartment = departmentRepository.findById(2L).get();
        int resultSizeEmployeeList = resultDepartment.getEmployeeList().size();

        Assertions.assertNotEquals(expectedName, oldName);
        Assertions.assertEquals(expectedName, resultDepartment.getName());
        Assertions.assertEquals(expectedSizeEmployeeList, resultSizeEmployeeList);
    }

    @Test
    void deleteById() {
        int expectedSize = departmentRepository.findAll().size();

        Department tempDepartment = new Department(null, "New department", List.of());
        tempDepartment = departmentRepository.save(tempDepartment);

        int resultSizeBefore = departmentRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        departmentRepository.deleteById(tempDepartment.getId());
        int result = departmentRepository.findAll().size();


        Assertions.assertEquals(expectedSize, result);

    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"
    })
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Department> department = departmentRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, department.isPresent());
        department.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @Test
    void findAll() {
        int expectedSize = 4;
        int resultSize = departmentRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }


}