package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.model.Position;
import org.example.repository.EmployeeRepository;
import org.example.repository.PhoneRepository;
import org.example.servlet.dto.*;
import org.example.servlet.mapper.PhoneDtoMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class PhoneServiceImplTest {
    @InjectMocks
    private  PhoneServiceImpl phoneNumberService;
    @Mock
    private  PhoneRepository mockitoPhoneNumberRepository;
    @Mock
    private PhoneDtoMapper phoneDtoMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private Position position;


    @Test
    void save() {
        Long expectedId = 1L;
        final long phoneId = 1L;

        PhoneIncomingDto dto = new PhoneIncomingDto("+123 123 1111");
        Phone phoneNumber = new Phone(expectedId, "+123 123 1111", null);
        PhoneOutGoingDto phoneOutGoingDto=new PhoneOutGoingDto(expectedId, "+123 123 1111", null);
        final Employee employee = new Employee(expectedId, "f1 name", "l1 name", position, List.of(), List.of());
        final Optional<Employee> optional = Optional.of(employee);
        phoneNumber.setEmployee(employee);


        Mockito.when(mockitoPhoneNumberRepository.save(Mockito.any(Phone.class))).thenReturn(phoneNumber);
        Mockito
                .doReturn(optional)
                .when(employeeRepository).findById(1L);
        Mockito
                .doReturn(phoneOutGoingDto)
                .when(phoneDtoMapper).map(any(Phone.class));
        Mockito.when(phoneDtoMapper.map(dto)).thenReturn(phoneNumber);

        PhoneOutGoingDto result = phoneNumberService.save(dto,phoneId);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        PhoneUpdateDto dto = new PhoneUpdateDto(expectedId, "+123 123 1111", null);
        Phone phoneNumber = new Phone(expectedId, "+123 123 1111", null);
        PhoneOutGoingDto phoneOutGoingDto=new PhoneOutGoingDto(expectedId, "+123 123 1111", null);

        Mockito.when(mockitoPhoneNumberRepository.save(Mockito.any(Phone.class))).thenReturn(phoneNumber);
        Mockito.when(phoneDtoMapper.map(dto)).thenReturn(phoneNumber);
        Mockito.when(phoneDtoMapper.map(phoneNumber)).thenReturn(phoneOutGoingDto);
        Mockito
                .doReturn(Optional.of(phoneNumber))
                .when(mockitoPhoneNumberRepository).findById(phoneNumber.getId());

        phoneNumberService.update(dto);

        ArgumentCaptor<Phone> argumentCaptor = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(mockitoPhoneNumberRepository).save(argumentCaptor.capture());

        Phone result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        Long expectedId = 1L;

        Phone phoneNumber = new Phone(expectedId, "+123 123 1111", null);
        PhoneUpdateDto dto = new PhoneUpdateDto(1L, "+123 123 1111", null);

        Mockito
                .when(mockitoPhoneNumberRepository.findById(phoneNumber.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(phoneDtoMapper.map(dto)).thenReturn(phoneNumber);


        ResponseStatusException e  = assertThrows(ResponseStatusException.class,
                () -> phoneNumberService.update(dto));

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Phone Not Found\""));
        Mockito.verify(mockitoPhoneNumberRepository, Mockito.times(1))
                .findById(phoneNumber.getId());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Phone> phone = Optional.of(new Phone(expectedId, "+123 123 1111", null));
        PhoneOutGoingDto phoneOutGoingDto=new PhoneOutGoingDto(expectedId, "+123 123 1111", null);

        Mockito.doReturn(phone).when(mockitoPhoneNumberRepository).findById(Mockito.anyLong());
        Mockito.when(phoneDtoMapper.map(phone.get())).thenReturn(phoneOutGoingDto);

        PhoneOutGoingDto dto = phoneNumberService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> phoneNumberService.findById(1L));

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Phone Not Found\""));
        Mockito.verify(mockitoPhoneNumberRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void findAll() {
        phoneNumberService.findAll();
        Mockito.verify(mockitoPhoneNumberRepository).findAll();
    }

    @Test
    void delete() {
        Long expectedId = 1L;

        Phone phoneNumber = new Phone(expectedId, "+123 123 1111", null);

        Mockito
                .doReturn(Optional.of(phoneNumber))
                .when(mockitoPhoneNumberRepository).findById(phoneNumber.getId());

        phoneNumberService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockitoPhoneNumberRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}