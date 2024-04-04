package org.example.repository.impl;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.example.db.PropertiesManager;
import org.example.model.Phone;
import org.example.repository.PhoneRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;

class PhoneRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static PhoneRepository phoneNumberRepository;
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
        phoneNumberRepository = PhoneRepositoryImpl.getInstance();
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
        String expectedNumber = "+3 (123) 123 4321";
        Phone phoneNumber = new Phone(
                null,
                expectedNumber,
                null
        );
        phoneNumber = phoneNumberRepository.save(phoneNumber);
        Optional<Phone> resultPhone = phoneNumberRepository.findById(phoneNumber.getId());

        Assertions.assertTrue(resultPhone.isPresent());
        Assertions.assertEquals(expectedNumber, resultPhone.get().getNumber());
    }

    @Test
    void update() {
        String expectedNumber = "+3 (321) 321 4321";

        Phone phoneNumberUpdate = phoneNumberRepository.findById(3L).get();
        String oldPhoneNumber = phoneNumberUpdate.getNumber();

        phoneNumberUpdate.setNumber(expectedNumber);
        phoneNumberRepository.update(phoneNumberUpdate);

        Phone number = phoneNumberRepository.findById(3L).get();

        Assertions.assertNotEquals(expectedNumber, oldPhoneNumber);
        Assertions.assertEquals(expectedNumber, number.getNumber());
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = phoneNumberRepository.findAll().size();

        Phone tempNumber = new Phone(null, "+(temp) number", null);
        tempNumber = phoneNumberRepository.save(tempNumber);

        int resultSizeBefore = phoneNumberRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = phoneNumberRepository.deleteById(tempNumber.getId());
        int resultSizeListAfter = phoneNumberRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeListAfter);
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteByEmployeeId() {
        Boolean expectedValue = true;
        int expectedSize = phoneNumberRepository.findAll().size() - phoneNumberRepository.findAllByEmployeeId(1L).size();

        boolean resultDelete = phoneNumberRepository.deleteByEmployeeId(1L);

        int resultSize = phoneNumberRepository.findAll().size();
        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Check exist by Phone number.")
    @ParameterizedTest
    @CsvSource(value = {
            "'+1(123)123 5555',true",
            "'not exits number', false"
    })
    void existsByNumber(String number, Boolean expectedValue) {
        boolean isExist = phoneNumberRepository.existsByNumber(number);

        Assertions.assertEquals(expectedValue, isExist);
    }

    @DisplayName("Find by Phone number.")
    @ParameterizedTest
    @CsvSource(value = {
            "'+1(123)123 5555',true",
            "'not exits number', false"
    })
    void findByNumber(String findNumber, Boolean expectedValue) {
        Optional<Phone> phoneNumber = phoneNumberRepository.findByNumber(findNumber);

        Assertions.assertEquals(expectedValue, phoneNumber.isPresent());
        phoneNumber.ifPresent(phone -> Assertions.assertEquals(findNumber, phone.getNumber()));
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"
    })
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Phone> phoneNumber = phoneNumberRepository.findById(expectedId);

        Assertions.assertEquals(expectedValue, phoneNumber.isPresent());
        phoneNumber.ifPresent(phone -> Assertions.assertEquals(expectedId, phone.getId()));
    }

    @Test
    void findAll() {
        int expectedSize = 9;
        int resultSize = phoneNumberRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "4, true",
            "1000, false"
    })
    void exitsById(Long expectedId, Boolean expectedValue) {
        Boolean resultValue = phoneNumberRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, resultValue);
    }

    @DisplayName("Find by UserId")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 2",
            "2, 2",
            "3, 1",
            "1000, 0"
    })
    void findAllByEmployeeId(Long userId, int expectedSize) {
        int resultSize = phoneNumberRepository.findAllByEmployeeId(userId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }
}