package org.example.servlet;

import jakarta.servlet.http.HttpServlet;
import org.example.exception.NotFoundException;
import org.example.service.PhoneService;
import org.example.servlet.dto.PhoneIncomingDto;
import org.example.servlet.dto.PhoneOutGoingDto;
import org.example.servlet.dto.PhoneUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/phone/*")
public class PhoneServlet extends HttpServlet {
    private final  PhoneService phoneService;

    @Autowired
    public PhoneServlet(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Transactional
    @GetMapping("/all")
    public List<PhoneOutGoingDto> getAuthors() throws SQLException {
        return phoneService.findAll();
    }

    @Transactional
    @GetMapping("/{phoneId}")
    public PhoneOutGoingDto getPhoneById(@PathVariable long phoneId) throws SQLException, NotFoundException {
        return phoneService.findById(phoneId);
    }

    @Transactional
    @PostMapping("/{phoneId}")
    public PhoneOutGoingDto postAuthor(@RequestBody PhoneIncomingDto phoneIncomingDto, @PathVariable long phoneId) throws SQLException {
        return phoneService.save(phoneIncomingDto,phoneId);
    }

    @Transactional
    @DeleteMapping("/{phoneId}")
    public void deleteAuthor(@PathVariable long phoneId) throws SQLException {
        phoneService.delete(phoneId);
    }

    @PutMapping
    @Transactional
    public void putAuthor(@RequestBody PhoneUpdateDto phoneUpdateDto) throws SQLException, NotFoundException {
        phoneService.update(phoneUpdateDto);
    }

}
