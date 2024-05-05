package org.example.servlet;


import jakarta.servlet.http.HttpServlet;
import org.example.exception.NotFoundException;
import org.example.service.EmployeeService;
import org.example.servlet.dto.EmployeeIncomingDto;
import org.example.servlet.dto.EmployeeOutGoingDto;
import org.example.servlet.dto.EmployeeUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/employee/*")
public class EmployeeServlet extends HttpServlet {
    private  EmployeeService employeeService;




    @Autowired
    public EmployeeServlet(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping(value = "/all")
    @Transactional
    public List<EmployeeOutGoingDto> getEmployeeAll() throws IOException {
        return employeeService.findAll();
    }

    @GetMapping(value = "/{employeeId}")
    @Transactional
    public EmployeeOutGoingDto getEmployeeById(@PathVariable long employeeId) throws NotFoundException {
        return employeeService.findById(employeeId);
    }


    @PostMapping
    @Transactional
    public EmployeeOutGoingDto postEmployee(@RequestBody EmployeeIncomingDto employeeDto) throws SQLException {
        return employeeService.save(employeeDto);
    }

    @PutMapping
    @Transactional
    public void putEmployee(@RequestBody EmployeeUpdateDto employeeUpdateDto) throws SQLException, NotFoundException, IOException {
        employeeService.updateEmployee(employeeUpdateDto);
    }

    @Transactional
    @DeleteMapping("/{employeeId}")
    public void deletePosition(@PathVariable long employeeId) throws SQLException, NotFoundException {
        employeeService.delete(employeeId);
    }


}
