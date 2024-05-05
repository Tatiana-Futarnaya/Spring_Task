package org.example.servlet.dto;

import java.util.Collection;
import java.util.List;

public class DepartmentOutGoingDto {
    private Long id;
    private String name;


    public DepartmentOutGoingDto() {
    }

    public DepartmentOutGoingDto(Long id, String name) {
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



}
