package org.example.service;

import org.example.exception.NotFoundException;
import org.example.servlet.dto.DepartmentIncomingDto;
import org.example.servlet.dto.DepartmentOutGoingDto;
import org.example.servlet.dto.DepartmentUpdateDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DepartmentService {
    DepartmentOutGoingDto save(DepartmentIncomingDto department);

    void update(DepartmentUpdateDto department) throws NotFoundException;

    DepartmentOutGoingDto findById(Long departmentId) throws NotFoundException;

    List<DepartmentOutGoingDto> findAll();

    void delete(Long departmentId) throws NotFoundException;



}
