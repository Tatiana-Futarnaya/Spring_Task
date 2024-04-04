package org.example.servlet.dto;

import java.util.List;

public class DepartmentOutGoingDto {
    private Long id;
    private String name;
    private List<EmployeeSmallOutGoingDto> employeeList;

    public DepartmentOutGoingDto() {
    }

    public DepartmentOutGoingDto(Long id, String name, List<EmployeeSmallOutGoingDto> employeeList) {
        this.id = id;
        this.name = name;
        this.employeeList = employeeList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeSmallOutGoingDto> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeSmallOutGoingDto> employeeList) {
        this.employeeList = employeeList;
    }
}
