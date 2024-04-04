package org.example.repository;

import org.example.model.Phone;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends Repository<Phone, Long> {
    List<Phone> findAllByEmployeeId(Long employeeId);

    boolean deleteByEmployeeId(Long employeeId);

    boolean existsByNumber(String number);

    Optional<Phone> findByNumber(String number);
}
