package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.*;
import org.example.repository.*;

import java.sql.*;
import java.util.*;

import static org.example.repository.impl.SQLRequest.*;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private static EmployeeRepository instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private final EmployeeToDepartmentRepository employeeToDepartmentRepository = EmployeeToDepartmentRepositoryImpl.getInstance();
    private final PhoneRepository phoneRepository = PhoneRepositoryImpl.getInstance();
    private final PositionRepository positionRepository = PositionRepositoryImpl.getInstance();
    private final DepartmentRepository departmentRepository = DepartmentRepositoryImpl.getInstance();

    private EmployeeRepositoryImpl() {
    }

    public static synchronized EmployeeRepository getInstance() {
        if (instance == null) {
            instance = new EmployeeRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Employee save(Employee employee) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_EMPLOYEE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, employee.getEmployeeFirstName());
            preparedStatement.setString(2, employee.getEmployeeLastName());
            if (employee.getPosition() == null) {
                preparedStatement.setNull(3, Types.NULL);
            } else {
                preparedStatement.setLong(3, employee.getPosition().getId());
            }
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                employee = new Employee(
                        resultSet.getLong("employee_id"),
                        employee.getEmployeeFirstName(),
                        employee.getEmployeeLastName(),
                        employee.getPosition(),
                        null,
                        null
                );
            }
            savePhoneList(employee);
            saveDepartmentList(employee);
            employee.getPhoneNumberList();
            employee.getDepartmentList();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return employee;
    }



    private void saveDepartmentList(Employee employee) {
        if (employee.getDepartmentList() != null && !employee.getDepartmentList().isEmpty()) {
            List<Long> departmentIdList = new ArrayList<>(
                    employee.getDepartmentList()
                            .stream()
                            .map(Department::getId)
                            .toList()
            );
            List<EmployeeToDepartment> employeeToDepartmentList = employeeToDepartmentRepository.findAllByEmployeeId(employee.getId());
            for (EmployeeToDepartment employeeToDepartment : employeeToDepartmentList) {
                if (!departmentIdList.contains(employeeToDepartment.getDepartmentId())) {
                    employeeToDepartmentRepository.deleteById(employeeToDepartment.getId());
                }
                departmentIdList.remove(employeeToDepartment.getDepartmentId());
            }
            for (Long departmentId : departmentIdList) {
                if (departmentRepository.exitsById(departmentId)) {
                    EmployeeToDepartment employeeToDepartment = new EmployeeToDepartment(
                            null,
                            employee.getId(),
                            departmentId
                    );
                    employeeToDepartmentRepository.save(employeeToDepartment);
                }
            }

        } else {
            employeeToDepartmentRepository.deleteByEmployeeId(employee.getId());
        }
    }


    private void savePhoneList(Employee employee) {
        if (employee.getPhoneNumberList() != null && !employee.getPhoneNumberList().isEmpty()) {
            List<Phone> phoneList = new ArrayList<>(employee.getPhoneNumberList());
            List<Long> phoneIdList = new ArrayList<>(
                    phoneRepository.findAllByEmployeeId(employee.getId())
                            .stream()
                            .map(Phone::getId)
                            .toList()
            );

            for (int i = 0; i < phoneList.size(); i++) {
                Phone phoneNumber = phoneList.get(i);
                phoneNumber.setEmployee(employee);
                if (phoneIdList.contains(phoneNumber.getId())) {
                    phoneRepository.update(phoneNumber);
                } else {
                    saveOrUpdateExitsNumber(phoneNumber);
                }
                phoneList.set(i, null);
                phoneIdList.remove(phoneNumber.getId());
            }
            phoneList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(phoneNumber -> {
                        phoneNumber.setEmployee(employee);
                        phoneRepository.save(phoneNumber);
                    });
            phoneIdList
                    .forEach(phoneRepository::deleteById);
        } else {
            phoneRepository.deleteByEmployeeId(employee.getId());
        }

    }



    private void saveOrUpdateExitsNumber(Phone phoneNumber) {
        if (phoneRepository.existsByNumber(phoneNumber.getNumber())) {
            Optional<Phone> number = phoneRepository.findByNumber(phoneNumber.getNumber());
            if (number.isPresent()
                && number.get().getEmployee() != null
                && number.get().getEmployee().getId() > 0) {
                phoneNumber = new Phone(number.get().getId(),
                        number.get().getNumber(),
                        number.get().getEmployee()
                );
                phoneRepository.update(phoneNumber);

            }
        } else {
            phoneRepository.save(phoneNumber);
        }

    }

    @Override
    public void update(Employee employee) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_SQL)) {

            preparedStatement.setString(1, employee.getEmployeeFirstName());
            preparedStatement.setString(2, employee.getEmployeeLastName());
            if (employee.getPosition() == null) {
                preparedStatement.setNull(3, Types.NULL);
            } else {
                preparedStatement.setLong(3, employee.getPosition().getId());
            }
            preparedStatement.setLong(4, employee.getId());

            preparedStatement.executeUpdate();
            savePhoneList(employee);
            saveDepartmentList(employee);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_SQL)) {

            employeeToDepartmentRepository.deleteByEmployeeId(id);
            phoneRepository.deleteByEmployeeId(id);

            preparedStatement.setLong(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        Employee employee = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_EMPLOYEE_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employee = createEmployee(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(employee);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employeeList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_EMPLOYEE_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Employee employee= createEmployee(resultSet);
                employeeList.add(employee);

            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return employeeList;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_EMPLOYEE_SQL)) {

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

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        Long employeeId = resultSet.getLong("employee_id");
        Long id=resultSet.getLong("position_id");
        Position position = positionRepository.findById(id).orElse(null);

        return new Employee(
                employeeId,
                resultSet.getString("employee_firstName"),
                resultSet.getString("employee_lastName"),
                position,
                null,
                null
        );
    }
}
