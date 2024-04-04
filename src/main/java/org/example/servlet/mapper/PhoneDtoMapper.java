package org.example.servlet.mapper;

import org.example.model.Phone;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;

import java.util.List;

public interface PhoneDtoMapper {
    Phone map(PhoneIncomingDto phoneNumberIncomingDto);

    PhoneOutGoingDto map(Phone phoneNumber);

    List<PhoneOutGoingDto> map(List<Phone> phoneNumberList);

    List<Phone> mapUpdateList(List<PhoneUpdateDto> phoneNumberUpdateList);

    Phone map(PhoneUpdateDto phoneNumberIncomingDto);
}
