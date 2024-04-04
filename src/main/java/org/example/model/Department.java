package org.example.model;

import org.example.repository.EmployeeToDepartmentRepository;
import org.example.repository.impl.EmployeeToDepartmentRepositoryImpl;

import java.util.List;


public class Department {
    private static final EmployeeToDepartmentRepository employeeToDepartmentRepository = EmployeeToDepartmentRepositoryImpl.getInstance();
    private Long id;
    private String name;
    private List<Employee> employeeList;

    public Department() {
    }

    public Department(Long id, String name, List<Employee> employeeList) {
        this.id = id;
        this.name = name;
        this.employeeList = employeeList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployeeList() {
        if (employeeList == null) {
            employeeList = employeeToDepartmentRepository.findEmployeesByDepartmentId(this.id);
        }
        return employeeList;
    }


    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeList=" + employeeList +
                '}';
    }
}
