package org.example.servlet.mapper.impl;


import org.example.model.Employee;
import org.example.model.Phone;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.example.servlet.mapper.PhoneDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class PhoneDtoMapperImplTest {
    private PhoneDtoMapper phoneNumberDtoMapper;

    @BeforeEach
    void setUp() {
        phoneNumberDtoMapper = PhoneDtoMapperImpl.getInstance();
    }

    @DisplayName("Phone map(PhoneIncomingDto")
    @Test
    void mapIncoming() {
        PhoneIncomingDto dto = new PhoneIncomingDto("+1 111 232 1234");

        Phone result = phoneNumberDtoMapper.map(dto);
        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getNumber(), result.getNumber());
    }

    @DisplayName("PhoneOutGoingDto map(Phone)")
    @Test
    void testMapOutgoing() {
        Phone phone = new Phone(
                100L,
                "+1 123 123 1234",
                new Employee(3L,
                        "f1",
                        "f2",
                        null,
                        List.of(),
                        List.of())
        );

        PhoneOutGoingDto result = phoneNumberDtoMapper.map(phone);

        Assertions.assertEquals(phone.getId(), result.getId());
        Assertions.assertEquals(phone.getNumber(), result.getNumber());
        Assertions.assertEquals(phone.getEmployee().getId(), result.getEmployeeDto().getId());
    }

    @DisplayName("List<PhoneOutGoingDto> map(List<Phone>)")
    @Test
    void testMapList() {
        List<Phone> phoneList = List.of(
                new Phone(
                        100L,
                        "+1 123 123 1234",
                        new Employee(3L,
                                "f1",
                                "f2",
                                null,
                                List.of(),
                                List.of())
                ),
                new Phone(
                        101L,
                        "+1 222 333 1234",
                        new Employee(4L,
                                "f3",
                                "f4",
                                null,
                                List.of(),
                                List.of())
                )

        );

        List<PhoneOutGoingDto> result = phoneNumberDtoMapper.map(phoneList);

        Assertions.assertEquals(phoneList.size(), result.size());
    }

    @DisplayName("List<PhoneNumber> mapUpdateList(List<PhoneNumberUpdateDto>")
    @Test
    void mapUpdateList() {
        List<PhoneUpdateDto> updateDtoList = List.of(
                new PhoneUpdateDto(
                        100L,
                        "+1 123 123 1234", 1L
                ),
                new PhoneUpdateDto(
                        101L,
                        "+1 222 333 1234",
                        2L
                )
        );

        List<Phone> result = phoneNumberDtoMapper.mapUpdateList(updateDtoList);

        Assertions.assertEquals(updateDtoList.size(), result.size());
    }

    @DisplayName("Phone map(PhoneUpdateDto)")
    @Test
    void testMapUpdate() {
        PhoneUpdateDto dto = new PhoneUpdateDto(
                100L,
                "+1 123 123 1234",
                1L
        );

        Phone result = phoneNumberDtoMapper.map(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getNumber(), result.getNumber());
    }
}