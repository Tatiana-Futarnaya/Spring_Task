package org.example.servlet.dto;


public class PhoneUpdateDto {
    private Long id;
    private String number;
    private Long employeeId;

    public PhoneUpdateDto() {
    }

    public PhoneUpdateDto(Long id, String number, Long employeeId) {
        this.id = id;
        this.number = number;
        this.employeeId = employeeId;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
