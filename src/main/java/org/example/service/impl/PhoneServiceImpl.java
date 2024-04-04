package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Phone;
import org.example.repository.PhoneRepository;
import org.example.repository.impl.PhoneRepositoryImpl;
import org.example.service.PhoneService;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.example.servlet.mapper.PhoneDtoMapper;
import org.example.servlet.mapper.impl.PhoneDtoMapperImpl;

import java.util.List;

public class PhoneServiceImpl implements PhoneService {
    private final PhoneDtoMapper phoneNumberDtoMapper = PhoneDtoMapperImpl.getInstance();
    private final PhoneRepository phoneNumberRepository = PhoneRepositoryImpl.getInstance();
    private static PhoneService instance;


    private PhoneServiceImpl() {
    }

    public static synchronized PhoneService getInstance() {
        if (instance == null) {
            instance = new PhoneServiceImpl();
        }
        return instance;
    }

    @Override
    public PhoneOutGoingDto save(PhoneIncomingDto phoneNumberDto) {
        Phone phoneNumber = phoneNumberDtoMapper.map(phoneNumberDto);
        phoneNumber = phoneNumberRepository.save(phoneNumber);
        return phoneNumberDtoMapper.map(phoneNumber);
    }

    @Override
    public void update(PhoneUpdateDto phoneNumberUpdateDto) throws NotFoundException {
        if (phoneNumberRepository.exitsById(phoneNumberUpdateDto.getId())) {
            Phone phoneNumber = phoneNumberDtoMapper.map(phoneNumberUpdateDto);
            phoneNumberRepository.update(phoneNumber);
        } else {
            throw new NotFoundException("Phone not found.");
        }
    }

    @Override
    public PhoneOutGoingDto findById(Long phoneNumberId) throws NotFoundException {
        Phone phoneNumber = phoneNumberRepository.findById(phoneNumberId).orElseThrow(() ->
                new NotFoundException("Phone not found."));
        return phoneNumberDtoMapper.map(phoneNumber);
    }

    @Override
    public List<PhoneOutGoingDto> findAll() {
        List<Phone> phoneNumberList = phoneNumberRepository.findAll();
        return phoneNumberDtoMapper.map(phoneNumberList);
    }

    @Override
    public boolean delete(Long phoneNumberId) {
        return phoneNumberRepository.deleteById(phoneNumberId);
    }

}
