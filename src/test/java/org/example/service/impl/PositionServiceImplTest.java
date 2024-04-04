package org.example.service.impl;

import org.example.exception.NotFoundException;

import org.example.model.Position;
import org.example.repository.PositionRepository;
import org.example.repository.impl.PositionRepositoryImpl;
import org.example.service.PositionService;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

class PositionServiceImplTest {
    private static PositionService positionService;
    private static PositionRepository mockPositionRepository;
    private static PositionRepositoryImpl oldInstance;

    private static void setMock(PositionRepository mock) {
        try {
            Field instance = PositionRepositoryImpl.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (PositionRepositoryImpl) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockPositionRepository = Mockito.mock(PositionRepository.class);
        setMock(mockPositionRepository);
        positionService = PositionServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = PositionRepositoryImpl.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockPositionRepository);
    }

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

        Mockito.doReturn(true).when(mockPositionRepository).exitsById(Mockito.any());

        positionService.update(dto);

        ArgumentCaptor<Position> argumentCaptor = ArgumentCaptor.forClass(Position.class);
        Mockito.verify(mockPositionRepository).update(argumentCaptor.capture());

        Position result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        PositionUpdateDto dto = new PositionUpdateDto(1L, "position update #1");

        Mockito.doReturn(false).when(mockPositionRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> positionService.update(dto), "Not found."
        );
        Assertions.assertEquals("Position not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Long expectedId = 1L;

        Optional<Position> position = Optional.of(new Position(expectedId, "position found #1"));

        Mockito.doReturn(true).when(mockPositionRepository).exitsById(Mockito.any());
        Mockito.doReturn(position).when(mockPositionRepository).findById(Mockito.anyLong());

        PositionOutGoingDto dto = positionService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Mockito.doReturn(false).when(mockPositionRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> positionService.findById(1L), "Not found."
        );
        Assertions.assertEquals("Position not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        positionService.findAll();
        Mockito.verify(mockPositionRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Long expectedId = 100L;

        Mockito.doReturn(true).when(mockPositionRepository).exitsById(100L);

        positionService.delete(expectedId);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockPositionRepository).deleteById(argumentCaptor.capture());

        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}