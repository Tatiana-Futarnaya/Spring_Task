package org.example.servlet.dto;

import org.example.model.Position;

public class EmployeeIncomingDto {
    private String firstName;
    private String lastName;
    private Position position;

    public EmployeeIncomingDto() {
    }

    public EmployeeIncomingDto(String firstName, String lastName, Position position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Position getPosition() {
        return position;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


}

