package org.example.servlet;
/**
 * @author Tatiana Futarnaya
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.exception.NotFoundException;
import org.example.model.Position;
import org.example.repository.PositionRepository;
import org.example.service.PositionService;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.example.servlet.dto.PositionIncomingDto;
import org.example.servlet.dto.PositionOutGoingDto;
import org.example.servlet.dto.PositionUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping({"/position/*"})
public class PositionServlet extends HttpServlet {
    private final  PositionService positionService;


    @Autowired
    public PositionServlet(PositionService positionService) {
        this.positionService = positionService;
    }


    @GetMapping("/all")
    @Transactional
    public List<PositionOutGoingDto> getPositionAll() throws SQLException {
        return positionService.findAll();
    }

    @GetMapping("/{positionId}")
    @Transactional
    public PositionOutGoingDto getPositionById(@PathVariable long positionId) throws SQLException, NotFoundException {
        return positionService.findById(positionId);
    }

    @PostMapping
    public PositionOutGoingDto postPosition(@RequestBody PositionIncomingDto positionIncomingDto) throws SQLException {
        return positionService.save(positionIncomingDto);
    }

    @DeleteMapping("/{positionId}")
    public void deletePosition(@PathVariable long positionId) throws SQLException, NotFoundException {
        positionService.delete(positionId);
    }

    @PutMapping("/{positionId}")
    @Transactional
    public void putPosition(@RequestBody PositionUpdateDto positionUpdateDto, @PathVariable long positionId) throws SQLException, NotFoundException, IOException {
        positionService.update(positionUpdateDto);
    }
}
