package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Position;
import org.example.repository.PositionRepository;
import org.example.repository.impl.PositionRepositoryImpl;
import org.example.service.PositionService;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.example.servlet.mapper.PositionDtoMapper;
import org.example.servlet.mapper.impl.PositionDtoMapperImpl;

import java.util.List;

public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository = PositionRepositoryImpl.getInstance();
    private final PositionDtoMapper positionDtoMapper = PositionDtoMapperImpl.getInstance();
    private static PositionService instance;

    private PositionServiceImpl() {
    }

    public static synchronized PositionService getInstance() {
        if (instance == null) {
            instance = new PositionServiceImpl();
        }
        return instance;
    }

    @Override
    public PositionOutGoingDto save(PositionIncomingDto positionDto) {
        Position position = positionDtoMapper.map(positionDto);
        position = positionRepository.save(position);
        return positionDtoMapper.map(position);
    }

    @Override
    public void update(PositionUpdateDto positionDto) throws NotFoundException {
        checkPositionExist(positionDto.getId());
        Position role = positionDtoMapper.map(positionDto);
        positionRepository.update(role);
    }

    @Override
    public PositionOutGoingDto findById(Long positionId) throws NotFoundException {
        Position position = positionRepository.findById(positionId).orElseThrow(() ->
                new NotFoundException("Position not found."));
        return positionDtoMapper.map(position);
    }

    @Override
    public List<PositionOutGoingDto> findAll() {
        List<Position> positionList = positionRepository.findAll();
        return positionDtoMapper.map(positionList);
    }

    @Override
    public boolean delete(Long positionId) throws NotFoundException {
        checkPositionExist(positionId);
        return positionRepository.deleteById(positionId);
    }

    private void checkPositionExist(Long positionId) throws NotFoundException {
        if (!positionRepository.exitsById(positionId)) {
            throw new NotFoundException("Position not found.");
        }
    }
}
