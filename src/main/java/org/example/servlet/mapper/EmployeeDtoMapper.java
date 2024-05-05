package org.example.servlet.mapper;

import org.example.model.Employee;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;

import java.util.List;


@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {DepartmentDtoMapper.class, PhoneDtoMapper.class})
public interface EmployeeDtoMapper {


    @Mapping(source = "employeeIncomingDto.firstName",target = "employeeFirstName")
    @Mapping(source = "employeeIncomingDto.lastName",target = "employeeLastName")
    @Mapping(source = "employeeIncomingDto.position",target = "position")
    Employee map(EmployeeIncomingDto employeeIncomingDto);



    @Mapping(source ="employeeUpdateDto.firstName" ,target = "employeeFirstName")
    @Mapping(source ="employeeUpdateDto.lastName" ,target = "employeeLastName")
    @Mapping(source ="employeeUpdateDto.position" ,target = "position")
    @Mapping(source ="employeeUpdateDto.phoneNumberList" ,target = "phoneNumberList")
    @Mapping(source ="employeeUpdateDto.departmentList" ,target = "departmentList")
    Employee map(EmployeeUpdateDto employeeUpdateDto);

    @Mapping(source ="employee.employeeFirstName" ,target = "firstName")
    @Mapping(source ="employee.employeeLastName" ,target = "lastName")
    @Mapping(source ="employee.position" ,target = "position")
    @Mapping(source ="employee.phoneNumberList" ,target = "phoneNumberList")
    @Mapping(source ="employee.departmentList" ,target = "departmentList")
    EmployeeOutGoingDto map(Employee employee);

    default List<EmployeeOutGoingDto> map(List<Employee> employeeList){
        return employeeList.stream().map(this::map).toList();
    }


}
