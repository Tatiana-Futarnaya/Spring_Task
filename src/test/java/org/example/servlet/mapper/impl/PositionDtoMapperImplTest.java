package org.example.servlet.mapper.impl;

import org.example.model.Position;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.example.servlet.mapper.PositionDtoMapper;
import org.junit.jupiter.api.*;

import java.util.List;

class PositionDtoMapperImplTest {
    private static Position position;
    private static PositionIncomingDto positionIncomingDto;
    private static PositionUpdateDto positionUpdateDto;
    private PositionDtoMapper positionDtoMapper;

    @BeforeAll
    static void beforeAll() {
        position = new Position(
                10L,
                "Position for Test"
        );

        positionIncomingDto = new PositionIncomingDto(
                "Incoming dto"
        );

        positionUpdateDto = new PositionUpdateDto(
                100L,
                "Update dto"
        );
    }

    @BeforeEach
    void setUp() {
        positionDtoMapper = PositionDtoMapperImpl.getInstance();
    }

    @DisplayName("Position map(PositionIncomingDto)")
    @Test
    void mapIncoming() {
        Position resultPosition = positionDtoMapper.map(positionIncomingDto);

        Assertions.assertNull(resultPosition.getId());
        Assertions.assertEquals(positionIncomingDto.getName(), resultPosition.getName());
    }

    @DisplayName("Position map(RoleUpdateDto)")
    @Test
    void testMapUpdate() {
        Position resultPosition = positionDtoMapper.map(positionUpdateDto);

        Assertions.assertEquals(positionUpdateDto.getId(), resultPosition.getId());
        Assertions.assertEquals(positionUpdateDto.getName(), resultPosition.getName());
    }

    @DisplayName("PositionOutGoingDto map(Position)")
    @Test
    void testMapOutgoing() {
        PositionOutGoingDto resulPosition = positionDtoMapper.map(position);

        Assertions.assertEquals(position.getId(), resulPosition.getId());
        Assertions.assertEquals(position.getName(), resulPosition.getName());
    }


    @DisplayName("List<PositionOutGoingDto> map(List<Position> positionList")
    @Test
    void testMapList() {
        List<PositionOutGoingDto> resultList = positionDtoMapper.map(
                List.of(
                        position,
                        position,
                        position
                )
        );

        Assertions.assertEquals(3, resultList.size());
    }
}