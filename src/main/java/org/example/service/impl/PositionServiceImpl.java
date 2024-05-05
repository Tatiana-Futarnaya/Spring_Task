package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Position;
import org.example.repository.PositionRepository;
import org.example.service.PositionService;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.example.servlet.mapper.PositionDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository ;
    private final PositionDtoMapper positionDtoMapper;



    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, PositionDtoMapper positionDtoMapper) {
        this.positionRepository = positionRepository;
        this.positionDtoMapper = positionDtoMapper;
    }

    @Override
    public PositionOutGoingDto save(PositionIncomingDto positionDto) {
        Position position = positionDtoMapper.map(positionDto);
        position = positionRepository.save(position);
        return positionDtoMapper.map(position);
    }

    @Override
    public void update(PositionUpdateDto positionDto) throws NotFoundException {
        positionRepository.findById(positionDto.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position Not Found"));
        Position position = positionDtoMapper.map(positionDto);
        positionRepository.save(position);
    }

    @Override
    public PositionOutGoingDto findById(Long positionId) throws NotFoundException {
        Position position = positionRepository.findById(positionId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Position Not Found"));
        return positionDtoMapper.map(position);
    }

    @Override
    public List<PositionOutGoingDto> findAll() {
        List<Position> positions = positionRepository.findAll();

        return positions.stream()
                .map(position -> positionDtoMapper.map(position))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long positionId) throws NotFoundException {
        positionRepository.findById(positionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found"));
        positionRepository.deleteById(positionId);
    }


}
