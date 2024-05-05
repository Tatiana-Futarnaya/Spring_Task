package org.example.servlet.dto;

import org.springframework.beans.factory.annotation.Autowired;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPosition(PositionOutGoingDto position) {
        this.position = position;
    }

    public void setPhoneNumberList(List<PhoneOutGoingDto> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public void setDepartmentList(List<DepartmentOutGoingDto> departmentList) {
        this.departmentList = departmentList;
    }


}
