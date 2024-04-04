package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Position;
import org.example.repository.PositionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.repository.impl.SQLRequest.*;

public class PositionRepositoryImpl implements PositionRepository {

    private static PositionRepository instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private PositionRepositoryImpl() {
    }

    public static synchronized PositionRepository getInstance() {
        if (instance == null) {
            instance = new PositionRepositoryImpl();
        }
        return instance;
    }

    private static Position createPosition(ResultSet resultSet) throws SQLException {
        Position position;
        position = new Position(resultSet.getLong("position_id"),
                resultSet.getString("position_title"));
        return position;
    }

    @Override
    public Position save(Position position) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_POSITION_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, position.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                position = new Position(
                        resultSet.getLong("position_id"),
                        position.getName());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return position;
    }

    @Override
    public void update(Position position) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION_SQL)) {

            preparedStatement.setString(1, position.getName());
            preparedStatement.setLong(2, position.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_POSITION_SQL)) {

            preparedStatement.setLong(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public Optional<Position> findById(Long id) {
        Position position = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_POSITION_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                position = createPosition(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(position);
    }

    @Override
    public List<Position> findAll() {
        List<Position> positionList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_POSITION_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                positionList.add(createPosition(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return positionList;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_POSITION_SQL)) {

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
