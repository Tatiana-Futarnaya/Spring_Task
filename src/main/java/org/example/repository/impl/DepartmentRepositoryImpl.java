package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Department;
import org.example.repository.DepartmentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.repository.impl.SQLRequest.*;

public class DepartmentRepositoryImpl implements DepartmentRepository {
    private static DepartmentRepository instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private DepartmentRepositoryImpl() {
    }

    public static synchronized DepartmentRepository getInstance() {
        if (instance == null) {
            instance = new DepartmentRepositoryImpl();
        }
        return instance;
    }

    private static Department createDepartment(ResultSet resultSet) throws SQLException {
        Department department;
        department = new Department(
                resultSet.getLong("department_id"),
                resultSet.getString("department_name"),
                null);
        return department;
    }

    @Override
    public Department save(Department department) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_DEPARTMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, department.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                department = new Department(
                        resultSet.getLong("department_id"),
                        department.getName(),
                        null
                );
                department.getEmployeeList();
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return department;
    }

    @Override
    public void update(Department department) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DEPARTMENT_SQL)) {

            preparedStatement.setString(1, department.getName());
            preparedStatement.setLong(2, department.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DEPARTMENT_SQL)) {

            preparedStatement.setLong(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public Optional<Department> findById(Long id) {
        Department department = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_DEPARTMENT_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                department = createDepartment(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(department);
    }

    @Override
    public List<Department> findAll() {
        List<Department> roleList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_DEPARTMENT_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roleList.add(createDepartment(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return roleList;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_DEPARTMENT_SQL)) {

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
}
