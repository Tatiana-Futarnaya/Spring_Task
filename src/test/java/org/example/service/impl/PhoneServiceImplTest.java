package org.example.service.impl;

import org.example.exception.NotFoundException;

import org.example.model.Phone;
import org.example.repository.PhoneRepository;
import org.example.repository.impl.PhoneRepositoryImpl;
import org.example.service.PhoneService;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

class PhoneServiceImplTest {
    private static PhoneService phoneNumberService;
    private static PhoneRepository mockitoPhoneNumberRepository;
    private static PhoneRepositoryImpl oldInstance;

    private static void setMock(PhoneRepository mock) {
        try {
            Field instance = PhoneRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (PhoneRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockitoPhoneNumberRepository = Mockito.mock(PhoneRepository.class);
        setMock(mockitoPhoneNumberRepository);
        phoneNumberService = PhoneServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = PhoneRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockitoPhoneNumberRepository);
    }

    @Test
    void save() {
        Long expectedId = 1L;

        PhoneIncomingDto dto = new PhoneIncomingDto("+123 123 1111");
        Phone phoneNumber = new Phone(expectedId, "+123 123 1111", null);

        Mockito.doReturn(phoneNumber).when(mockitoPhoneNumberRepository).save(Mockito.any(Phone.class));

        PhoneOutGoingDto result = phoneNumberService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        PhoneUpdateDto dto = new PhoneUpdateDto(expectedId, "+123 123 1111", null);

        Mockito.doReturn(true).when(mockitoPhoneNumberRepository).exitsById(Mockito.any());

        phoneNumberService.update(dto);

        ArgumentCaptor<Phone> argumentCaptor = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(mockitoPhoneNumberRepository).update(argumentCaptor.capture());

        Phone result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        PhoneUpdateDto dto = new PhoneUpdateDto(1L, "+123 123 1111", null);

        Mockito.doReturn(false).when(mockitoPhoneNumberRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> phoneNumberService.update(dto), "Not found."
        );
        Assertions.assertEquals("PhoneNumber not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Phone> role = Optional.of(new Phone(expectedId, "+123 123 1111", null));

        Mockito.doReturn(true).when(mockitoPhoneNumberRepository).exitsById(Mockito.any());
        Mockito.doReturn(role).when(mockitoPhoneNumberRepository).findById(Mockito.anyLong());

        PhoneOutGoingDto dto = phoneNumberService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Mockito.doReturn(false).when(mockitoPhoneNumberRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> phoneNumberService.findById(1L), "Not found."
        );
        Assertions.assertEquals("PhoneNumber not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        phoneNumberService.findAll();
        Mockito.verify(mockitoPhoneNumberRepository).findAll();
    }

    @Test
    void delete() {
        Long expectedId = 100L;

        phoneNumberService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockitoPhoneNumberRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}