package org.example.servlet.mapper;

import org.example.model.Employee;
import org.example.model.Phone;
import org.example.servlet.dto.EmployeeSmallOutGoingDto;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring", uses = {EmployeeDtoMapper.class})
public interface PhoneDtoMapper {



    Phone map(PhoneIncomingDto phoneNumberIncomingDto);



    default PhoneOutGoingDto map(Phone phoneNumber){
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

    @Mapping(source ="phone.number" ,target = "number")
    List<PhoneOutGoingDto> map(List<Phone> phoneNumberList);


    @Mapping(source ="phoneUpdateDto.id" ,target = "id")
    @Mapping(source ="phoneUpdateDto.number" ,target = "number")
    @Mapping(source ="phoneUpdateDto.employeeId" ,target = "employee")
    List<Phone> mapUpdateList(List<PhoneUpdateDto> phoneNumberUpdateList);



    default Phone map(PhoneUpdateDto updateDto){
        return new Phone(
                updateDto.getId(),
                updateDto.getNumber(),
                new Employee(
                        updateDto.getEmployeeId(),
                        null,
                        null,
                        null,
                        List.of(),
                        List.of()
                )
        );
    }



    Phone map(Long id, PhoneIncomingDto phoneNumberDto);
}
