package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Department;
import org.example.model.Employee;
import org.example.model.EmployeeToDepartment;
import org.example.repository.DepartmentRepository;
import org.example.repository.EmployeeRepository;
import org.example.repository.EmployeeToDepartmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.repository.impl.SQLRequest.*;

public class EmployeeToDepartmentRepositoryImpl implements EmployeeToDepartmentRepository {
    private static final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static final DepartmentRepository departmentRepository = DepartmentRepositoryImpl.getInstance();
    private static final EmployeeRepository employeeRepository = EmployeeRepositoryImpl.getInstance();

    private static EmployeeToDepartmentRepository instance;

    private EmployeeToDepartmentRepositoryImpl() {
    }

    public static synchronized EmployeeToDepartmentRepository getInstance() {
        if (instance == null) {
            instance = new EmployeeToDepartmentRepositoryImpl();
        }
        return instance;
    }

    private static EmployeeToDepartment createEmployeeToDepartament(ResultSet resultSet) throws SQLException {
        EmployeeToDepartment employeeToDepartment;
        employeeToDepartment = new EmployeeToDepartment(
                resultSet.getLong("employee_department_id"),
                resultSet.getLong("employee_id"),
                resultSet.getLong("department_id")
        );
        return employeeToDepartment;
    }

    @Override
    public EmployeeToDepartment save(EmployeeToDepartment employeeToDepartment) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, employeeToDepartment.getEmployeeId());
            preparedStatement.setLong(2, employeeToDepartment.getDepartmentId());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                employeeToDepartment = new EmployeeToDepartment(
                        resultSet.getLong("employee_department_id"),
                        employeeToDepartment.getEmployeeId(),
                        employeeToDepartment.getDepartmentId()
                );
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return employeeToDepartment;
    }

    @Override
    public void update(EmployeeToDepartment employeeToDepartment) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setLong(1, employeeToDepartment.getEmployeeId());
            preparedStatement.setLong(2, employeeToDepartment.getDepartmentId());
            preparedStatement.setLong(3, employeeToDepartment.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public boolean deleteByEmployeeId(Long employeeId) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_EMPLOYEE_ID_SQL)) {

            preparedStatement.setLong(1, employeeId);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public boolean deleteByDepartmentId(Long departmentId) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_DEPARTMENT_ID_SQL)) {

            preparedStatement.setLong(1, departmentId);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public Optional<EmployeeToDepartment> findById(Long id) {
        EmployeeToDepartment employeeToDepartment = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employeeToDepartment = createEmployeeToDepartament(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(employeeToDepartment);
    }

    @Override
    public List<EmployeeToDepartment> findAll() {
        List<EmployeeToDepartment> employeeToDepartmentsList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employeeToDepartmentsList.add(createEmployeeToDepartament(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeToDepartmentsList;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return isExists;
    }

    public List<EmployeeToDepartment> findAllByEmployeeId(Long employeeId) {
        List<EmployeeToDepartment> employeeToDepartmentList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_EMPLOYEE_ID_SQL)) {

            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employeeToDepartmentList.add(createEmployeeToDepartament(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeToDepartmentList;
    }

    @Override
    public List<Department> findDepartmentsByEmployeeId(Long employeeId) {
        List<Department> departmentList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_EMPLOYEE_ID_SQL)) {

            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long departamentId = resultSet.getLong("department_id");
                Optional<Department> optionalDepartment = departmentRepository.findById(departamentId);
                optionalDepartment.ifPresent(departmentList::add);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return departmentList;
    }

    public List<EmployeeToDepartment> findAllByDepartmentId(Long departmentId) {
        List<EmployeeToDepartment> employeeToDepartmentList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_DEPARTMENT_ID_SQL)) {

            preparedStatement.setLong(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employeeToDepartmentList.add(createEmployeeToDepartament(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeToDepartmentList;
    }

    public List<Employee> findEmployeesByDepartmentId(Long departmentId) {
        List<Employee> employeeList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_DEPARTMENT_ID_SQL)) {

            preparedStatement.setLong(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long employeeId = resultSet.getLong("employee_id");
                Optional<Employee> optionalUser = employeeRepository.findById(employeeId);
                optionalUser.ifPresent(employeeList::add);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeList;
    }

    @Override
    public Optional<EmployeeToDepartment> findByEmployeeIdAndDepartmentId(Long employeeId, Long departmentId) {
        Optional<EmployeeToDepartment> employeeToDepartment = Optional.empty();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMPLOYEE_ID_AND_DEPARTMENT_ID_SQL)) {

            preparedStatement.setLong(1, employeeId);
            preparedStatement.setLong(2, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employeeToDepartment = Optional.of(createEmployeeToDepartament(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeToDepartment;
    }

}
