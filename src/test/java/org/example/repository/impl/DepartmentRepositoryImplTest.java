package org.example.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.db.PropertiesManager;
import org.example.model.Department;
import org.example.repository.DepartmentRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;


class DepartmentRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static DepartmentRepository departmentRepository;
    private static final int containerPort = 5432;
    private static final int localPort = 5432;
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("postgres")
            .withUsername(PropertiesManager.getProperties("db.username"))
            .withPassword(PropertiesManager.getProperties("db.password"))
            .withExposedPorts(containerPort)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
            ))
            .withInitScript(INIT_SQL);
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        departmentRepository = DepartmentRepositoryImpl.getInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, INIT_SQL);
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
        departmentRepository.update(department);

        Department resultDepartment = departmentRepository.findById(2L).get();
        int resultSizeEmployeeList = resultDepartment.getEmployeeList().size();

        Assertions.assertNotEquals(expectedName, oldName);
        Assertions.assertEquals(expectedName, resultDepartment.getName());
        Assertions.assertEquals(expectedSizeEmployeeList, resultSizeEmployeeList);
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = departmentRepository.findAll().size();

        Department tempDepartment = new Department(null, "New department", List.of());
        tempDepartment = departmentRepository.save(tempDepartment);

        int resultSizeBefore = departmentRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = departmentRepository.deleteById(tempDepartment.getId());
        int result = departmentRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
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

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "4; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long departmentId, Boolean expectedValue) {
        boolean isRoleExist = departmentRepository.exitsById(departmentId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }
}