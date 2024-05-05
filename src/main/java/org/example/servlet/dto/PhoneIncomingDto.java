package org.example.servlet.dto;


import org.example.model.Employee;

public class PhoneIncomingDto {
    private String number;
    private Employee employee;

    public PhoneIncomingDto() {
    }

    public PhoneIncomingDto(String number) {
        this.number = number;
    }

    public PhoneIncomingDto(String number, Employee employee) {
        this.number = number;
        this.employee = employee;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
