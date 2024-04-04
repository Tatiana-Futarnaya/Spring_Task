package org.example.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.db.PropertiesManager;
import org.example.model.EmployeeToDepartment;
import org.example.repository.EmployeeToDepartmentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;


class EmployeeToDepartmentRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static EmployeeToDepartmentRepository employeeToDepartmentRepository;
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
        employeeToDepartmentRepository = EmployeeToDepartmentRepositoryImpl.getInstance();
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
        Long expectedEmployeeId = 1L;
        Long expectedDepartmentId = 4L;
        EmployeeToDepartment link = new EmployeeToDepartment(
                null,
                expectedEmployeeId,
                expectedDepartmentId
        );
        link = employeeToDepartmentRepository.save(link);
        Optional<EmployeeToDepartment> resultLink = employeeToDepartmentRepository.findById(link.getId());

        Assertions.assertTrue(resultLink.isPresent());
        Assertions.assertEquals(expectedEmployeeId, resultLink.get().getEmployeeId());
        Assertions.assertEquals(expectedDepartmentId, resultLink.get().getDepartmentId());
    }

    @Test
    void update() {
        Long expectedEmployeeId = 1L;
        Long expectedDepartmentId = 4L;

        EmployeeToDepartment link = employeeToDepartmentRepository.findById(2L).get();

        Long oldDepartmentId = link.getDepartmentId();
        Long oldUserId = link.getEmployeeId();

        Assertions.assertNotEquals(expectedEmployeeId, oldUserId);
        Assertions.assertNotEquals(expectedDepartmentId, oldDepartmentId);

        link.setEmployeeId(expectedEmployeeId);
        link.setDepartmentId(expectedDepartmentId);

        employeeToDepartmentRepository.update(link);

        EmployeeToDepartment resultLink = employeeToDepartmentRepository.findById(2L).get();

        Assertions.assertEquals(link.getId(), resultLink.getId());
        Assertions.assertEquals(expectedEmployeeId, resultLink.getEmployeeId());
        Assertions.assertEquals(expectedDepartmentId, resultLink.getDepartmentId());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = employeeToDepartmentRepository.findAll().size();

        EmployeeToDepartment link = new EmployeeToDepartment(null, 1L, 3L);
        link = employeeToDepartmentRepository.save(link);

        int resultSizeBefore = employeeToDepartmentRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = employeeToDepartmentRepository.deleteById(link.getId());

        int resultSizeAfter = employeeToDepartmentRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @DisplayName("Delete by EmployeeId.")
    @ParameterizedTest
    @CsvSource(value = {
            "2, true",
            "1000, false"
    })
    void deleteByEmployeeId(Long expectedEmployeeId, Boolean expectedValue) {
        int beforeSize = employeeToDepartmentRepository.findAllByEmployeeId(expectedEmployeeId).size();
        Boolean resultDelete = employeeToDepartmentRepository.deleteByEmployeeId(expectedEmployeeId);

        int afterDelete = employeeToDepartmentRepository.findAllByEmployeeId(expectedEmployeeId).size();

        Assertions.assertEquals(expectedValue, resultDelete);
        if (beforeSize != 0) {
            Assertions.assertNotEquals(beforeSize, afterDelete);
        }
    }

    @DisplayName("Delete by Department Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "2, true",
            "1000, false"
    })
    void deleteByDepartmentId(Long expectedDepartmentId, Boolean expectedValue) {
        int beforeSize = employeeToDepartmentRepository.findAllByDepartmentId(expectedDepartmentId).size();
        Boolean resultDelete = employeeToDepartmentRepository.deleteByDepartmentId(expectedDepartmentId);

        int afterDelete = employeeToDepartmentRepository.findAllByDepartmentId(expectedDepartmentId).size();

        Assertions.assertEquals(expectedValue, resultDelete);
        if (beforeSize != 0) {
            Assertions.assertNotEquals(beforeSize, afterDelete);
        }
    }

    @DisplayName("Delete by Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true, 1, 1",
            "3, true, 3, 2",
            "1000, false, 0, 0"
    })
    void findById(Long expectedId, Boolean expectedValue, Long expectedEmployeeId, Long expectedDepartmentId) {
        Optional<EmployeeToDepartment> link = employeeToDepartmentRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, link.isPresent());
        if (link.isPresent()) {
            Assertions.assertEquals(expectedId, link.get().getId());
            Assertions.assertEquals(expectedEmployeeId, link.get().getEmployeeId());
            Assertions.assertEquals(expectedDepartmentId, link.get().getDepartmentId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 8;
        int resultSize = employeeToDepartmentRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "1000, false"
    })
    void exitsById(Long expectedId, Boolean expectedValue) {
        Boolean resultValue = employeeToDepartmentRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, resultValue);
    }

    @DisplayName("Find by employee Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1",
            "6, 2",
            "1000, 0"
    })
    void findAllByEmployeeId(Long employeeId, int expectedSize) {
        int resultSize = employeeToDepartmentRepository.findAllByEmployeeId(employeeId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find by employee Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "3, 1",
            "6, 2",
            "1000, 0"
    })
    void findDepartmentsByEmployeeId(Long employeeId, int expectedSize) {
        int resultSize = employeeToDepartmentRepository.findDepartmentsByEmployeeId(employeeId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Department by employee Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 3",
            "2, 3",
            "3, 1",
            "1000, 0"
    })
    void findAllByDepartmentId(Long departmentId, int expectedSize) {
        int resultSize = employeeToDepartmentRepository.findAllByDepartmentId(departmentId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find employee by Department Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 3",
            "2, 3",
            "3, 1",
            "1000, 0"
    })
    void findEmployeeByDepartmentId(Long departmentId, int expectedSize) {
        int resultSize = employeeToDepartmentRepository.findEmployeesByDepartmentId(departmentId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find employee by Department Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, true",
            "1, 4, false"
    })
    void findByEmployeeIdAndDepartmentId(Long employeeId, Long departmentId, Boolean expectedValue) {
        Optional<EmployeeToDepartment> link = employeeToDepartmentRepository.findByEmployeeIdAndDepartmentId(employeeId, departmentId);

        Assertions.assertEquals(expectedValue, link.isPresent());
    }
}