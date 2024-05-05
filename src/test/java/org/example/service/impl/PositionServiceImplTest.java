package org.example.service.impl;

import org.example.exception.NotFoundException;
import org.example.model.Position;
import org.example.repository.PositionRepository;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.example.servlet.mapper.PositionDtoMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PositionServiceImplTest {
    @InjectMocks
    private  PositionServiceImpl positionService;
    @Mock
    private  PositionRepository mockPositionRepository;
    @Spy
    private PositionDtoMapper positionDtoMapper= Mappers.getMapper(PositionDtoMapper.class);


    @Test
    void save() {
        Long expectedId = 1L;

        PositionIncomingDto dto = new PositionIncomingDto("position #2");
        Position position = new Position(expectedId, "position #10");

        Mockito.doReturn(position).when(mockPositionRepository).save(Mockito.any(Position.class));

        PositionOutGoingDto result = positionService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long expectedId = 1L;

        PositionUpdateDto dto = new PositionUpdateDto(expectedId, "position update #1");
        Position position = new Position(expectedId, "position #10");


        Mockito.when(mockPositionRepository.save(Mockito.any(Position.class))).thenReturn(position);
        Mockito.when(positionDtoMapper.map(dto)).thenReturn(position);

        Mockito
                .doReturn(Optional.of(position))
                .when(mockPositionRepository).findById(position.getId());

        positionService.update(dto);

        ArgumentCaptor<Position> argumentCaptor = ArgumentCaptor.forClass(Position.class);
        Mockito.verify(mockPositionRepository).save(argumentCaptor.capture());

        Position result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        Long expectedId = 1L;
        Position position = new Position(expectedId, "position #10");
        PositionUpdateDto dto = new PositionUpdateDto(1L, "position update #1");

        Mockito
                .when(mockPositionRepository.findById(position.getId()))
                .thenReturn(Optional.empty());


        ResponseStatusException e  = assertThrows(ResponseStatusException.class,
                () -> positionService.update(dto));

        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Position Not Found\""));
        Mockito.verify(mockPositionRepository, Mockito.times(1))
                .findById(position.getId());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Position> position = Optional.of(new Position(expectedId, "position found #1"));
        PositionOutGoingDto positionOutGoingDto=new PositionOutGoingDto(expectedId, "position update #1");

        Mockito.doReturn(position).when(mockPositionRepository).findById(Mockito.anyLong());
        Mockito.when(positionDtoMapper.map(position.get())).thenReturn(positionOutGoingDto);

        PositionOutGoingDto dto = positionService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
        Mockito.verify(mockPositionRepository, Mockito.times(1))
                .findById(expectedId);
    }

    @Test
    void findByIdNotFound() {

        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> positionService.findById(1L));
        assertThat(e.getMessage(), equalTo("404 NOT_FOUND \"Position Not Found\""));
        Mockito.verify(mockPositionRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void findAll() {
        positionService.findAll();
        Mockito.verify(mockPositionRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 1L;

        Position position = new Position(expectedId, "position #10");
        Mockito
                .doReturn(Optional.of(position))
                .when(mockPositionRepository).findById(position.getId());

        positionService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockPositionRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}