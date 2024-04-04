package org.example.model;

import org.example.repository.EmployeeRepository;
import org.example.repository.impl.EmployeeRepositoryImpl;


public class Phone {
    private static final EmployeeRepository employeeRepository = EmployeeRepositoryImpl.getInstance();
    private Long id;
    private String number;
    private Employee employee;

    public Phone() {
    }

    public Phone(Long id, String number, Employee employee) {
        this.id = id;
        this.number = number;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Employee getEmployee() {
        if (employee != null && employee.getId() > 0 && employee.getEmployeeFirstName() == null) {
            this.employee = employeeRepository.findById(employee.getId()).orElse(employee);
        } else if (employee != null && employee.getId() == 0) {
            this.employee = null;
        }
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", employee=" + employee +
                '}';
    }
}