package org.example.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.db.PropertiesManager;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.model.Position;
import org.example.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;


import java.util.List;
import java.util.Optional;

class EmployeeRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
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
    public static EmployeeRepository employeeRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        employeeRepository = EmployeeRepositoryImpl.getInstance();
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
        employeeRepository.update(employeeForUpdate);

        Employee resultEmployee = employeeRepository.findById(3L).get();

        Assertions.assertEquals(expectedFirstname, resultEmployee.getEmployeeFirstName());
        Assertions.assertEquals(expectedLastname, resultEmployee.getEmployeeLastName());

        Assertions.assertEquals(phoneListSize, resultEmployee.getPhoneNumberList().size());
        Assertions.assertEquals(departmentListSize, resultEmployee.getDepartmentList().size());
        Assertions.assertEquals(oldPosition.getId(), resultEmployee.getPosition().getId());

        employeeForUpdate.setPhoneNumberList(List.of());
        employeeForUpdate.setDepartmentList(List.of());
        employeeForUpdate.setPosition(new Position(expectedPositionId, null));
        employeeRepository.update(employeeForUpdate);
        resultEmployee = employeeRepository.findById(3L).get();

        Assertions.assertEquals(0, resultEmployee.getPhoneNumberList().size());
        Assertions.assertEquals(0, resultEmployee.getDepartmentList().size());
        Assertions.assertEquals(expectedPositionId, resultEmployee.getPosition().getId());

        departmentList.add(new Department(3L, null, null));
        departmentList.add(new Department(4L, null, null));
        employeeForUpdate.setDepartmentList(departmentList);
        employeeRepository.update(employeeForUpdate);
        resultEmployee = employeeRepository.findById(3L).get();

        Assertions.assertEquals(3, resultEmployee.getDepartmentList().size());

        departmentList.remove(2);
        employeeForUpdate.setDepartmentList(departmentList);
        employeeRepository.update(employeeForUpdate);
        resultEmployee = employeeRepository.findById(3L).get();

        Assertions.assertEquals(2, resultEmployee.getDepartmentList().size());

        employeeForUpdate.setPhoneNumberList(List.of(
                new Phone(null, "new phone", null),
                new Phone(null, "+1(123)123 2222", null)));
        employeeForUpdate.setDepartmentList(List.of(new Department(1L, null, null)));

        employeeRepository.update(employeeForUpdate);
        resultEmployee = employeeRepository.findById(3L).get();

        System.out.println(resultEmployee.getPhoneNumberList().size());

        Assertions.assertEquals(1, resultEmployee.getPhoneNumberList().size());
        Assertions.assertEquals(1, resultEmployee.getDepartmentList().size());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
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

        boolean resultDelete = employeeRepository.deleteById(tempeEmployee.getId());
        int positionListAfterSize = employeeRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, positionListAfterSize);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "4; true",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Employee> employee = employeeRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, employee.isPresent());
        employee.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @Test
    void findAll() {
        int expectedSize = 7;
        int resultSize = employeeRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "4; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long positionId, Boolean expectedValue) {
        boolean isEmployeeExist = employeeRepository.exitsById(positionId);

        Assertions.assertEquals(expectedValue, isEmployeeExist);
    }
}