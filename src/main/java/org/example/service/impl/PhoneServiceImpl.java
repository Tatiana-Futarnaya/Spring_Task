package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Employee;
import org.example.model.Phone;
import org.example.repository.EmployeeRepository;
import org.example.repository.PhoneRepository;
import org.example.service.PhoneService;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.example.servlet.mapper.PhoneDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneServiceImpl implements PhoneService {
    private final PhoneDtoMapper phoneNumberDtoMapper;
    private final PhoneRepository phoneNumberRepository;
    private final EmployeeRepository employeeRepository;


@Autowired
    public PhoneServiceImpl(PhoneDtoMapper phoneNumberDtoMapper, PhoneRepository phoneNumberRepository, EmployeeRepository employeeRepository) {
        this.phoneNumberDtoMapper = phoneNumberDtoMapper;
        this.phoneNumberRepository = phoneNumberRepository;
        this.employeeRepository=employeeRepository;
    }

    @Override
    public PhoneOutGoingDto save(PhoneIncomingDto phoneNumberDto, long phoneId) {
        Employee employee = employeeRepository.findById(phoneId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found")
        );


        Phone phone = phoneNumberDtoMapper.map(phoneNumberDto);
        phone.setEmployee(employee);


        phone = phoneNumberRepository.save(phone);
        return phoneNumberDtoMapper.map(phone);
    }

    @Override
    public void update(PhoneUpdateDto phoneNumberUpdateDto) throws NotFoundException {
        Phone phone=phoneNumberDtoMapper.map(phoneNumberUpdateDto);

        Phone oldPhone=phoneNumberRepository.findById(phone.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone Not Found"));

        //phoneNumberRepository.update(phone.getId(),phone.getNumber(),phone.getEmployee().getId());

       phone.setEmployee(oldPhone.getEmployee());
        phoneNumberRepository.save(phone);

      phoneNumberDtoMapper.map(phone);
    }

    @Override
    public PhoneOutGoingDto findById(Long phoneNumberId) throws NotFoundException {
        Phone phoneNumber = phoneNumberRepository.findById(phoneNumberId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone Not Found"));
        return phoneNumberDtoMapper.map(phoneNumber);
    }

    @Override
    public List<PhoneOutGoingDto> findAll() {
        List<Phone> phones = phoneNumberRepository.findAll();

        return phones.stream()
                .map(phone -> phoneNumberDtoMapper.map(phone))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long phoneNumberId) {
        phoneNumberRepository.findById(phoneNumberId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone Not Found"));
        phoneNumberRepository.deleteById(phoneNumberId);
    }



}
