package org.example.model;


public class EmployeeToDepartment {
    private Long id;
    private Long employeeId;
    private Long departmentId;

    public EmployeeToDepartment() {
    }

    public EmployeeToDepartment(Long id, Long employeeId, Long departmentId) {
        this.id = id;
        this.employeeId = employeeId;
        this.departmentId = departmentId;
    }


    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
