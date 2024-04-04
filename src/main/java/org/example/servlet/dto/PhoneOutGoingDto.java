package org.example.servlet.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PhoneOutGoingDto {
    private Long id;
    private String number;
    @JsonProperty("employee")
    private EmployeeSmallOutGoingDto employeeDto;

    public PhoneOutGoingDto() {
    }

    public PhoneOutGoingDto(Long id, String number, EmployeeSmallOutGoingDto employeeDto) {
        this.id = id;
        this.number = number;
        this.employeeDto = employeeDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EmployeeSmallOutGoingDto getEmployeeDto() {
        return employeeDto;
    }

    public void setEmployeeDto(EmployeeSmallOutGoingDto employeeDto) {
        this.employeeDto = employeeDto;
    }
}
