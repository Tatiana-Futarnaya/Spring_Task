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

}
