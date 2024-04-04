package org.example.model;

import org.example.repository.PhoneRepository;
import org.example.repository.EmployeeToDepartmentRepository;
import org.example.repository.impl.PhoneRepositoryImpl;
import org.example.repository.impl.EmployeeToDepartmentRepositoryImpl;

import java.util.List;


public class Employee {
    private static final PhoneRepository phoneNumberRepository = PhoneRepositoryImpl.getInstance();
    private static final EmployeeToDepartmentRepository employeeToDepartmentRepository = EmployeeToDepartmentRepositoryImpl.getInstance();
    private Long id;
    private String employeeFirstName;
    private String employeeLastName;
    private Position position;
    private List<Phone> phoneNumberList;
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

    public Long getId() {
        return id;
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
        if (phoneNumberList == null) {
            this.phoneNumberList = phoneNumberRepository.findAllByEmployeeId(this.id);
        }
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<Phone> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public List<Department> getDepartmentList() {
        if (departmentList == null) {
            departmentList = employeeToDepartmentRepository.findDepartmentsByEmployeeId(this.id);
        }
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", employee_firstName='" + employeeFirstName + '\'' +
                ", employee_lastName='" + employeeLastName + '\'' +
                ", position=" + position +
                ", phoneNumberList=" + phoneNumberList +
                ", departmentList=" + departmentList +
                '}';
    }
}
