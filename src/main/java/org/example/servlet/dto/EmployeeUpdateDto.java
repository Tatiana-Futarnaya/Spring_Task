package org.example.servlet.dto;

import java.util.List;

public class EmployeeUpdateDto {
    private Long id;
    private String firstName;
    private String lastName;
    private PositionUpdateDto position;
    private List<PhoneUpdateDto> phoneNumberList;
    private List<DepartmentUpdateDto> departmentList;

    public EmployeeUpdateDto() {
    }

    public EmployeeUpdateDto(Long id, String firstName, String lastName, PositionUpdateDto position, List<PhoneUpdateDto> phoneNumberList, List<DepartmentUpdateDto> departmentList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumberList = phoneNumberList;
        this.departmentList = departmentList;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PositionUpdateDto getPosition() {
        return position;
    }

    public List<PhoneUpdateDto> getPhoneNumberList() {
        return phoneNumberList;
    }

    public List<DepartmentUpdateDto> getDepartmentList() {
        return departmentList;
    }

}

