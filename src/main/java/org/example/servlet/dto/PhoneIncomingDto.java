package org.example.servlet.dto;


public class PhoneIncomingDto {
    private String number;

    public PhoneIncomingDto() {
    }

    public PhoneIncomingDto(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
