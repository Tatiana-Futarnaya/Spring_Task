package org.example.servlet.mapper;

import org.example.model.Department;
import org.example.servlet.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {EmployeeDtoMapper.class})
public interface DepartmentDtoMapper {

    @Mapping(source ="departmentIncomingDto.name" ,target = "name")
    Department map(DepartmentIncomingDto departmentIncomingDto);



    default DepartmentOutGoingDto map(Department department){

        return new DepartmentOutGoingDto(
                department.getId(),
                department.getName()
        );
    }



    Department map(DepartmentUpdateDto departmentUpdateDto);


    default List<DepartmentOutGoingDto> map(List<Department> departmentList){
        if ( departmentList == null ) {
            return null;
        }

        List<DepartmentOutGoingDto> list = new ArrayList<>(departmentList.size());
        for ( Department department : departmentList ) {
            DepartmentOutGoingDto d=map( department );
            list.add( d );
        }

        return list;
    }


    @Mapping(source = "departmentUpdateDto.id", target = "id")
    List<Department> mapUpdateList(List<DepartmentUpdateDto> departmentList);
}
