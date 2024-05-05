package org.example.servlet.dto;

public class PositionOutGoingDto {
    private Long id;
    private String name;


    public PositionOutGoingDto() {
    }

    public PositionOutGoingDto(Long id) {
        this.id = id;
    }

    public PositionOutGoingDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
