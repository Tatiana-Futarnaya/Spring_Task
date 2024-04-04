package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Phone;
import org.example.model.Employee;
import org.example.repository.PhoneRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.repository.impl.SQLRequest.*;

public class PhoneRepositoryImpl implements PhoneRepository {
    private static final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static PhoneRepository instance;

    private PhoneRepositoryImpl() {
    }

    public static synchronized PhoneRepository getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryImpl();
        }
        return instance;
    }

    private static Phone createPhoneNumber(ResultSet resultSet) throws SQLException {
        Phone phoneNumber;
        Employee employee = new Employee(
                resultSet.getLong("employee_id"),
                null,
                null,
                null,
                List.of(),
                List.of()
        );
        phoneNumber = new Phone(
                resultSet.getLong("phone_id"),
                resultSet.getString("phone_number"),
                employee);
        return phoneNumber;
    }

    @Override
    public Phone save(Phone phoneNumber) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PHONE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, phoneNumber.getNumber());
            if (phoneNumber.getEmployee() == null) {
                preparedStatement.setNull(2, Types.NULL);
            } else {
                preparedStatement.setLong(2, phoneNumber.getEmployee().getId());
            }
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {

                phoneNumber = new Phone(
                        resultSet.getLong("phone_id"),
                        phoneNumber.getNumber(),
                        phoneNumber.getEmployee()
                );
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return phoneNumber;
    }

    @Override
    public void update(Phone phoneNumber) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PHONE_SQL)) {

            preparedStatement.setString(1, phoneNumber.getNumber());
            if (phoneNumber.getEmployee() == null) {
                preparedStatement.setNull(2, Types.NULL);
            } else {
                preparedStatement.setLong(2, phoneNumber.getEmployee().getId());
            }
            preparedStatement.setLong(3, phoneNumber.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult = true;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PHONE_SQL)) {

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
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_BY_EMPLOYEE_ID_PHONE_SQL)) {

            preparedStatement.setLong(1, employeeId);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public boolean existsByNumber(String number) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_NUMBER_PHONE_SQL)) {

            preparedStatement.setString(1, number);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return isExists;
    }

    @Override
    public Optional<Phone> findByNumber(String number) {
        Phone phoneNumber = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NUMBER_PHONE_SQL)) {

            preparedStatement.setString(1, number);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                phoneNumber = createPhoneNumber(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(phoneNumber);
    }

    @Override
    public Optional<Phone> findById(Long id) {
        Phone phoneNumber = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_PHONE_PHONE_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                phoneNumber = createPhoneNumber(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(phoneNumber);
    }

    @Override
    public List<Phone> findAll() {
        List<Phone> phoneNumberList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_PHONE_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                phoneNumberList.add(createPhoneNumber(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return phoneNumberList;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_PHONE_SQL)) {

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

    @Override
    public List<Phone> findAllByEmployeeId(Long employeeId) {
        List<Phone> phoneNumberList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_EMPLOYEE_ID_PHONE_SQL)) {

            preparedStatement.setLong(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                phoneNumberList.add(createPhoneNumber(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return phoneNumberList;
    }
}
