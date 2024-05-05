package org.example.servlet.dto;

public class EmployeeSmallOutGoingDto {
    private Long id;
    private String firstName;
    private String lastName;

    public EmployeeSmallOutGoingDto() {
    }

    public EmployeeSmallOutGoingDto(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public EmployeeSmallOutGoingDto(Long id) {
        this.id = id;
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
}
