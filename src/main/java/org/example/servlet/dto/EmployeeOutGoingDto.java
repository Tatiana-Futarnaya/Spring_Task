package org.example.servlet.dto;

import java.util.List;

public class EmployeeOutGoingDto {
    private Long id;
    private String firstName;
    private String lastName;

    private PositionOutGoingDto position;
    private List<PhoneOutGoingDto> phoneNumberList;
    private List<DepartmentOutGoingDto> departmentList;

    public EmployeeOutGoingDto() {
    }

    public EmployeeOutGoingDto(Long id, String firstName, String lastName, PositionOutGoingDto position, List<PhoneOutGoingDto> phoneNumberList, List<DepartmentOutGoingDto> departmentList) {
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

    public PositionOutGoingDto getPosition() {
        return position;
    }


    public List<PhoneOutGoingDto> getPhoneNumberList() {
        return phoneNumberList;
    }

    public List<DepartmentOutGoingDto> getDepartmentList() {
        return departmentList;
    }

}
