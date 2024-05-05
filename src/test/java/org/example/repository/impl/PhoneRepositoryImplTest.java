package org.example.repository.impl;


import org.example.config.PersistenceConfigForTest;
import org.example.model.Phone;
import org.example.repository.EmployeeRepository;
import org.example.repository.PhoneRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.example.config.PersistenceConfigForTest.postgres;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfigForTest.class, EmployeeRepository.class, PhoneRepository.class})
class PhoneRepositoryImplTest {

    @Autowired
    public  PhoneRepository phoneNumberRepository;


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
        phoneNumberRepository.save(phoneNumberUpdate);

        Phone number = phoneNumberRepository.findById(3L).get();

        Assertions.assertNotEquals(expectedNumber, oldPhoneNumber);
        Assertions.assertEquals(expectedNumber, number.getNumber());
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteById() {
        int expectedSize = phoneNumberRepository.findAll().size();

        Phone tempNumber = new Phone(null, "+(temp) number", null);
        tempNumber = phoneNumberRepository.save(tempNumber);

        int resultSizeBefore = phoneNumberRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        phoneNumberRepository.deleteById(tempNumber.getId());
        int resultSizeListAfter = phoneNumberRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSizeListAfter);
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



    @DisplayName("Find by EmployeeId")
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