package org.example.servlet;

import jakarta.servlet.http.HttpServlet;
import org.example.exception.NotFoundException;
import org.example.service.DepartmentService;
import org.example.servlet.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping( {"/department/*"})
public class DepartmentServlet extends HttpServlet {
   private final DepartmentService departmentService;

   @Autowired
    public DepartmentServlet(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }



    @GetMapping(value = "/all")
    @Transactional
    public List<DepartmentOutGoingDto> getDepartmentAll() throws IOException {
        return departmentService.findAll();
    }

    @GetMapping(value = "/{departmentId}")
    @Transactional
    public DepartmentOutGoingDto getDepartmentById(@PathVariable long departmentId) throws NotFoundException {
        return departmentService.findById(departmentId);
    }

    @PostMapping
    @Transactional
    public DepartmentOutGoingDto postDepartment(@RequestBody DepartmentIncomingDto departmentIncomingDto) throws SQLException {
        return departmentService.save(departmentIncomingDto);
    }

    @DeleteMapping("/{departmentId}")
    public void deletePosition(@PathVariable long departmentId) throws SQLException, NotFoundException {
        departmentService.delete(departmentId);
    }

    @PutMapping("/{departmentId}")
    @Transactional
    public void putPosition(@RequestBody DepartmentUpdateDto departmentUpdateDto) throws SQLException, NotFoundException, IOException {

        departmentService.update(departmentUpdateDto);
    }
}
