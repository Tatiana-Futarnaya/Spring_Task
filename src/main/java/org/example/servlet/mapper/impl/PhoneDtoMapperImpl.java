package org.example.servlet.mapper.impl;

import org.example.model.Phone;
import org.example.model.Employee;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.example.servlet.dto.EmployeeSmallOutGoingDto;
import org.example.servlet.mapper.PhoneDtoMapper;

import java.util.List;

public class PhoneDtoMapperImpl implements PhoneDtoMapper {
    private static PhoneDtoMapper instance;

    private PhoneDtoMapperImpl() {
    }

    public static synchronized PhoneDtoMapper getInstance() {
        if (instance == null) {
            instance = new PhoneDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Phone map(PhoneIncomingDto phoneDto) {
        return new Phone(
                null,
                phoneDto.getNumber(),
                null
        );
    }

    @Override
    public PhoneOutGoingDto map(Phone phoneNumber) {
        return new PhoneOutGoingDto(
                phoneNumber.getId(),
                phoneNumber.getNumber(),
                phoneNumber.getEmployee() == null ?
                        null :
                        new EmployeeSmallOutGoingDto(
                                phoneNumber.getEmployee().getId(),
                                phoneNumber.getEmployee().getEmployeeFirstName(),
                                phoneNumber.getEmployee().getEmployeeLastName()
                        )
        );
    }

    @Override
    public List<PhoneOutGoingDto> map(List<Phone> phoneNumberList) {
        return phoneNumberList.stream().map(this::map).toList();
    }

    @Override
    public List<Phone> mapUpdateList(List<PhoneUpdateDto> phoneNumberUpdateList) {
        return phoneNumberUpdateList.stream().map(this::map).toList();
    }

    @Override
    public Phone map(PhoneUpdateDto phoneDto) {
        return new Phone(
                phoneDto.getId(),
                phoneDto.getNumber(),
                new Employee(
                        phoneDto.getEmployeeId(),
                        null,
                        null,
                        null,
                        List.of(),
                        List.of()
                )
        );
    }

}
