package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PositionService {
    PositionOutGoingDto save(PositionIncomingDto positionDto);

    void update(PositionUpdateDto positionDto) throws NotFoundException;

    PositionOutGoingDto findById(Long positionId) throws NotFoundException;

    List<PositionOutGoingDto> findAll();

    void delete(Long positionId) throws NotFoundException;
}
