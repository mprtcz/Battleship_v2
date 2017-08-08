package com.spanish_inquisition.battleship.server.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 08.08.17.
 */
public class DatabaseController {
    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DATABASE = "jdbc:sqlite:history.db";

    private static void loadJDBCDriver() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerIfNotExists(PlayerEntity playerEntity) {
        loadJDBCDriver();
        try (Connection connection = DriverManager.getConnection(DATABASE);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists players (id, name, score);");
            PreparedStatement preparedStatement = connection.prepareStatement("insert into history values (?, ?, ?);");
            setSaveStatementStrings(preparedStatement, playerEntity);
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setSaveStatementStrings(PreparedStatement preparedStatement, PlayerEntity playerEntity) throws SQLException {
        preparedStatement.setString(1, String.valueOf(playerEntity.getId()));
        preparedStatement.setString(2, playerEntity.getName());
        preparedStatement.setString(3, String.valueOf(playerEntity.getScore()));
        preparedStatement.addBatch();
    }



    private static PlayerEntity createPlayerFromResult(ResultSet resultSet) throws SQLException {
        int id = Integer.valueOf(resultSet.getString("id"));
        String name = resultSet.getString("name");
        int score = Integer.valueOf(resultSet.getString("score"));
        return new PlayerEntity(id, name, score);
    }

    public static List<PlayerEntity> loadPlayers() {
        loadJDBCDriver();
        List<PlayerEntity> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("create table if not exists players (id, name, score);");
            ResultSet resultSet = statement.executeQuery("select * from players;");
            while (resultSet.next()) {
                list.add(createPlayerFromResult(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
