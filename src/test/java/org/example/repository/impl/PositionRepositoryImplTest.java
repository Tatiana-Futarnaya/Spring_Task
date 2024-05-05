package org.example.repository.impl;


import org.example.config.PersistenceConfigForTest;
import org.example.model.Position;
import org.example.repository.EmployeeRepository;
import org.example.repository.PositionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.example.config.PersistenceConfigForTest.postgres;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfigForTest.class, EmployeeRepository.class, PositionRepository.class})
class PositionRepositoryImplTest {

    @Autowired
    public  PositionRepository positionRepository;


    @BeforeAll
    static void beforeAll() {
        postgres.start();

    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }



    @Test
    void save() {
        String expectedName = "new Position Name";
        Position position = new Position(null, expectedName);
        position = positionRepository.save(position);
        Optional<Position> resultPosition = positionRepository.findById(position.getId());

        Assertions.assertTrue(resultPosition.isPresent());
        Assertions.assertEquals(expectedName, resultPosition.get().getName());
    }

    @Test
    void update() {
        String expectedName = "UPDATE Position Name";

        Position positionForUpdate = positionRepository.findById(3L).get();
        String oldPositionName = positionForUpdate.getName();

        positionForUpdate.setName(expectedName);
        positionRepository.save(positionForUpdate);

        Position position = positionRepository.findById(3L).get();

        Assertions.assertNotEquals(expectedName, oldPositionName);
        Assertions.assertEquals(expectedName, position.getName());
    }

    @DisplayName("Delete by ID")
    @Test
    void deleteById() {

        int expectedSize = positionRepository.findAll().size();

        Position tempPosition = new Position(null, "Position for delete.");
        tempPosition = positionRepository.save(tempPosition);

       positionRepository.deleteById(tempPosition.getId());
        int positionListAfterSize = positionRepository.findAll().size();

        Assertions.assertEquals(expectedSize, positionListAfterSize);
    }


    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "4; true",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        Optional<Position> position = positionRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, position.isPresent());
        position.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @Test
    void findAll() {
        int expectedSize = 5;
        int resultSize = positionRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

}