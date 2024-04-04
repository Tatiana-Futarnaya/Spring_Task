package org.example.repository.impl;

/**
 * @author Tatiana Futarnaya
 */
public final class SQLRequest {

    private SQLRequest() {
    }

    static final String SAVE_EMPLOYEE_SQL
            = "INSERT INTO employee (employee_firstName, employee_lastName,  position_id) VALUES (?, ? ,?)";
    static final String UPDATE_EMPLOYEE_SQL
            = "UPDATE employee SET employee_firstName = ?,employee_lastName = ?, position_id =?" +
            " WHERE employee_id = ?";
    static final String DELETE_EMPLOYEE_SQL = "DELETE FROM employee WHERE employee_id = ?";
    static final String FIND_BY_ID_EMPLOYEE_SQL
            = "SELECT * FROM employee " +
            "WHERE employee_id = ? LIMIT 1";
    static final String FIND_ALL_EMPLOYEE_SQL
            = "SELECT * FROM employee";
    static final String EXIST_BY_ID_EMPLOYEE_SQL
            = "SELECT exists (SELECT 1 FROM employee WHERE employee_id = ? LIMIT 1)";

    static final String SAVE_POSITION_SQL = "INSERT INTO position (position_title) VALUES (?)";
    static final String UPDATE_POSITION_SQL = "UPDATE position SET position_title = ? WHERE position_id = ?";
    static final String DELETE_POSITION_SQL = "DELETE FROM position WHERE position_id = ?";
    static final String FIND_BY_ID_POSITION_SQL
            = "SELECT * FROM position WHERE position_id = ? LIMIT 1";
    static final String FIND_ALL_POSITION_SQL = "SELECT * FROM position";
    static final String EXIST_BY_ID_POSITION_SQL
            = "SELECT exists (SELECT 1 FROM position WHERE position_id = ? LIMIT 1)";


    static final String SAVE_PHONE_SQL = "INSERT INTO phone (phone_number, employee_id) VALUES (?, ?)";
    static final String UPDATE_PHONE_SQL
            = "UPDATE phone SET phone_number = ?,employee_id = ? WHERE phone_id = ?";
    static final String DELETE_PHONE_SQL = "DELETE FROM phone WHERE phone_id = ?";
    static final String FIND_BY_ID_PHONE_PHONE_SQL
            = "SELECT * FROM phone WHERE phone_id = ? LIMIT 1";
    static final String FIND_BY_NUMBER_PHONE_SQL
            = "SELECT * FROM phone " +
            "WHERE phone_number = ? LIMIT 1";
    static final String EXIST_BY_NUMBER_PHONE_SQL
            = "SELECT exists (SELECT 1 FROM phone WHERE phone_number = LOWER(?) LIMIT 1)";
    static final String FIND_ALL_BY_EMPLOYEE_ID_PHONE_SQL
            = "SELECT * FROM phone WHERE employee_id = ?";
    static final String DELETE_ALL_BY_EMPLOYEE_ID_PHONE_SQL = "DELETE FROM phone WHERE employee_id = ?";
    static final String FIND_ALL_PHONE_SQL
            = "SELECT * FROM phone";
    static final String EXIST_BY_ID_PHONE_SQL
            = "SELECT exists (SELECT 1 FROM phone WHERE phone_id = ? LIMIT 1)";

    static final String SAVE_DEPARTMENT_SQL = "INSERT INTO departments (department_name) VALUES (?)";
    static final String UPDATE_DEPARTMENT_SQL = "UPDATE departments SET department_name = ? WHERE department_id = ?";
    static final String DELETE_DEPARTMENT_SQL = "DELETE FROM departments WHERE department_id = ?";
    static final String FIND_BY_ID_DEPARTMENT_SQL
            = "SELECT * FROM departments WHERE department_id = ? LIMIT 1";
    static final String FIND_ALL_DEPARTMENT_SQL = "SELECT * FROM departments";
    static final String EXIST_BY_ID_DEPARTMENT_SQL
            = "SELECT exists (SELECT 1 FROM departments WHERE department_id = ? LIMIT 1)";


    static final String SAVE_SQL = "INSERT INTO employee_departments (employee_id, department_id) VALUES (?, ?)";
    static final String UPDATE_SQL
            = "UPDATE employee_departments SET employee_id = ?,  department_id = ?  WHERE employee_department_id = ?";
    static final String DELETE_SQL = "DELETE FROM employee_departments WHERE employee_department_id = ?";
    static final String FIND_BY_ID_SQL
            = "SELECT * FROM employee_departments WHERE employee_department_id = ?  LIMIT 1";
    static final String FIND_ALL_SQL
            = "SELECT * FROM employee_departments";
    static final String FIND_ALL_BY_EMPLOYEE_ID_SQL
            = "SELECT * FROM employee_departments WHERE employee_id = ?";
    static final String FIND_ALL_BY_DEPARTMENT_ID_SQL
            = "SELECT * FROM employee_departments  WHERE department_id = ?";
    static final String FIND_BY_EMPLOYEE_ID_AND_DEPARTMENT_ID_SQL
            = "SELECT * FROM employee_departments WHERE employee_id = ? AND department_id = ? LIMIT 1";
    static final String DELETE_BY_EMPLOYEE_ID_SQL = "DELETE FROM employee_departments WHERE employee_id = ?";
    static final String DELETE_BY_DEPARTMENT_ID_SQL = "DELETE FROM employee_departments WHERE department_id = ?";
    static final String EXIST_BY_ID_SQL
            = "SELECT exists (SELECT 1 FROM employee_departments  WHERE employee_department_id = ?  LIMIT 1)";
}
