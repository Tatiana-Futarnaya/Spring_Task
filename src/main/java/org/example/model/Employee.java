package org.example.model;

import jakarta.persistence.*;
import org.apache.catalina.User;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String employeeFirstName;
    @Column(nullable = false)
    private String employeeLastName;
    @ManyToOne(fetch = FetchType.EAGER,cascade =  { CascadeType.REFRESH} )
    @JoinColumn(name="position_id", referencedColumnName = "id")
    private Position position;
    @OneToMany(mappedBy = "employee",fetch = FetchType.EAGER,orphanRemoval = true,
            cascade= {CascadeType.ALL})
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Phone> phoneNumberList;
    @ManyToMany(cascade = { CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "employee_departments", joinColumns = @JoinColumn(name = "employee_id",referencedColumnName = "id"),
            inverseJoinColumns=@JoinColumn(name = "department_id",referencedColumnName = "id"))
    private List<Department> departmentList;

    public Employee() {
    }

    public Employee(Long id, String firstName, String lastName, Position position, List<Phone> phoneNumberList, List<Department> departmentList) {
        this.id = id;
        this.employeeFirstName = firstName;
        this.employeeLastName = lastName;
        this.position = position;
        this.phoneNumberList = phoneNumberList;
        this.departmentList = departmentList;
    }

    public Employee(Long id, String employeeFirstName, String employeeLastName, Position position) {
        this.id = id;
        this.employeeFirstName = employeeFirstName;
        this.employeeLastName = employeeLastName;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Phone> getPhoneNumberList() {
        return phoneNumberList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setPhoneNumberList(List<Phone> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }


    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employeeFirstName='" + employeeFirstName + '\'' +
                ", employeeLastName='" + employeeLastName + '\'' +
                ", position=" + position +
                ", phoneNumberList=" + phoneNumberList +
                ", departmentList=" + departmentList +
                '}';
    }
}
