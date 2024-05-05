package org.example.repository;

import org.example.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {


    @Query(value = "SELECT exists (SELECT 1 FROM position WHERE position_id = ? LIMIT 1)", nativeQuery = true)
    boolean exitsById(Long id);
}
