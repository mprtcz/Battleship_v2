package com.spanish_inquisition.battleship.server.database;

import com.spanish_inquisition.battleship.common.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by michal on 08.08.17.
 */
public class DatabaseController {
    static String JDBC_DRIVER = "org.sqlite.JDBC";
    static final String DB_NAME = "history.db";
    static final String TABLE_NAME = "players";
    static String DATABASE = "jdbc:sqlite:dbs/" + DB_NAME;

    public DatabaseController() {
        loadJDBCDriver();
    }

    void saveOrUpdatePlayerEntity(PlayerEntity playerEntity) {
        if (checkIfPlayerExists(playerEntity, loadPlayers())) {
            updatePlayer(playerEntity);
        } else {
            savePlayer(playerEntity);
        }
    }

    private static void loadJDBCDriver() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            AppLogger.logger.log(Level.WARNING, "Exception occured", e);
        }
    }

    private void savePlayer(PlayerEntity playerEntity) {
        try (Connection connection = DriverManager.getConnection(DATABASE);
             Statement statement = connection.createStatement()) {
            createTableIfNotExists(statement);
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("insert into %s (name, score) values (?, ?);", TABLE_NAME));
            setSaveStatementStrings(preparedStatement, playerEntity);
            preparedStatement.executeBatch();
        } catch (Exception e) {
            AppLogger.logger.log(Level.WARNING, "Exception occured", e);
        }
    }

    private void updatePlayer(PlayerEntity playerEntity) {
        try (Connection connection = DriverManager.getConnection(DATABASE);
             Statement statement = connection.createStatement()) {
            createTableIfNotExists(statement);
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("update %s set score = ? where name = ?;", TABLE_NAME));
            setUpdateStatementStrings(preparedStatement, playerEntity);
            preparedStatement.executeBatch();
        } catch (Exception e) {
            AppLogger.logger.log(Level.WARNING, "Exception occured", e);
        }
    }

    private static void setSaveStatementStrings(PreparedStatement preparedStatement, PlayerEntity playerEntity) throws SQLException {
        preparedStatement.setString(1, playerEntity.getName());
        preparedStatement.setString(2, String.valueOf(playerEntity.getScore()));
        preparedStatement.addBatch();
    }

    private static void setUpdateStatementStrings(PreparedStatement preparedStatement, PlayerEntity playerEntity) throws SQLException {
        preparedStatement.setString(1, String.valueOf(playerEntity.getScore()));
        preparedStatement.setString(2, playerEntity.getName());
        preparedStatement.addBatch();
    }

    private static PlayerEntity createPlayerFromResult(ResultSet resultSet) throws SQLException {
        int id = Integer.valueOf(resultSet.getString("id"));
        String name = resultSet.getString("name");
        int score = Integer.valueOf(resultSet.getString("score"));
        return new PlayerEntity(id, name, score);
    }

    List<PlayerEntity> loadPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE);
             Statement statement = connection.createStatement()) {
            createTableIfNotExists(statement);
            fillListWithResults(list, statement);
        } catch (SQLException e) {
            AppLogger.logger.log(Level.WARNING, "Exception occured", e);
        }
        return list;
    }

    private void fillListWithResults(List<PlayerEntity> list, Statement statement) {
        try (ResultSet resultSet = statement.executeQuery(String.format("select * from %s;", TABLE_NAME))) {
            while (resultSet.next()) {
                list.add(createPlayerFromResult(resultSet));
            }
        } catch (SQLException e) {
            AppLogger.logger.log(Level.WARNING, "Exception occured", e);
        }
    }

    private void createTableIfNotExists(Statement statement) throws SQLException {
        statement.executeUpdate(String.format("create table if not exists %s (id INTEGER PRIMARY KEY, name VARCHAR, score INT);", TABLE_NAME));
    }

    private boolean checkIfPlayerExists(PlayerEntity playerEntity, List<PlayerEntity> playerEntities) {
        return playerEntities.contains(playerEntity);
    }
}
