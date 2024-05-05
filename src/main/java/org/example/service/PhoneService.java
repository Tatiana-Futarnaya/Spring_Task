package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PhoneService {
    PhoneOutGoingDto save(PhoneIncomingDto phoneNumber, long phoneId);

    void update(PhoneUpdateDto phoneNumber) throws NotFoundException;

    PhoneOutGoingDto findById(Long phoneNumberId) throws NotFoundException;

    List<PhoneOutGoingDto> findAll();

    void delete(Long phoneNumberId);
}
