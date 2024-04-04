package org.example.servlet.mapper;

import org.example.model.Position;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;

import java.util.List;

public interface PositionDtoMapper {
    Position map(PositionIncomingDto positionIncomingDto);

    Position map(PositionUpdateDto positionUpdateDto);

    PositionOutGoingDto map(Position position);

    List<PositionOutGoingDto> map(List<Position> positionList);
}
