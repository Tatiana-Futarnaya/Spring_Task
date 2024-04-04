package org.example.servlet.mapper.impl;

import org.example.model.Position;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.example.servlet.mapper.PositionDtoMapper;

import java.util.List;

public class PositionDtoMapperImpl implements PositionDtoMapper {
    private static PositionDtoMapper instance;

    private PositionDtoMapperImpl() {
    }

    public static synchronized PositionDtoMapper getInstance() {
        if (instance == null) {
            instance = new PositionDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Position map(PositionIncomingDto positionIncomingDto) {
        return new Position(
                null,
                positionIncomingDto.getName()
        );
    }

    @Override
    public Position map(PositionUpdateDto positionUpdateDto) {
        return new Position(
                positionUpdateDto.getId(),
                positionUpdateDto.getName());
    }

    @Override
    public PositionOutGoingDto map(Position position) {
        return new PositionOutGoingDto(
                position.getId(),
                position.getName()
        );
    }

    @Override
    public List<PositionOutGoingDto> map(List<Position> positionList) {
        return positionList.stream().map(this::map).toList();
    }
}
