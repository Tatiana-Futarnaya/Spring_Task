package org.example.servlet.mapper;

import org.example.model.Position;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring", uses = {EmployeeDtoMapper.class})
public interface PositionDtoMapper {

    @Mapping(source ="positionIncomingDto.name" ,target = "name")
    Position map(PositionIncomingDto positionIncomingDto);

    @Mapping(source ="positionUpdateDto.id" ,target = "id")
    Position map(PositionUpdateDto positionUpdateDto);

    @Mapping(source ="position.id" ,target = "id")
    PositionOutGoingDto map(Position position);

    List<PositionOutGoingDto> map(List<Position> positionList);
}
