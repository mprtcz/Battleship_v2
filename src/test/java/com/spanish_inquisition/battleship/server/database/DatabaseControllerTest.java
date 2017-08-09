package com.spanish_inquisition.battleship.server.database;

import javafx.embed.swing.JFXPanel;
import org.testng.annotations.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by michal on 09.08.17.
 */
public class DatabaseControllerTest {
    private DatabaseController databaseController;
    private static final String DB_NAME = "history-test.db";
    private static final String TABLE_NAME = DatabaseController.TABLE_NAME;

    @BeforeSuite
    public void setupJavaFx() {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });
    }


    @BeforeClass
    public void setUpDBController() {
        databaseController = new DatabaseController();
        DatabaseController.JDBC_DRIVER = "org.h2.Driver";
        DatabaseController.DATABASE = "jdbc:h2:./dbs/" + DB_NAME;
    }

    @AfterMethod
    private void dropAfterMethod() {
        try (Connection connection = DriverManager.getConnection(DatabaseController.DATABASE);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table " + TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    private void createTableBeforeMethod() {
        System.out.println("Creating a table");
        try (Connection connection = DriverManager.getConnection(DatabaseController.DATABASE);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("create table if not exists %s (id INT IDENTITY PRIMARY KEY, name VARCHAR, score INT);", TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveOrUpdatePlayerEntity() throws Exception {
        PlayerEntity playerEntity = new PlayerEntity(1, "Test", 1);
        databaseController.saveOrUpdatePlayerEntity(playerEntity);
        List<PlayerEntity> entities = databaseController.loadPlayers();
        assertEquals(entities.size(), 1);
    }

    @Test
    public void testLoadPlayers() throws Exception {
        insertTwoPlayersIntoDB();
        List<PlayerEntity> entities = databaseController.loadPlayers();
        System.out.println("entities = " + entities);
        assertEquals(entities.size(), 2);
        assertTrue(entities.contains(new PlayerEntity("test1", 1)));
        assertTrue(entities.contains(new PlayerEntity("test2", 1)));
    }

    @Test
    public void testUpdatePlayer() {
        insertTwoPlayersIntoDB();
        PlayerEntity playerEntity = new PlayerEntity("test1", 2);
        databaseController.saveOrUpdatePlayerEntity(playerEntity);
        List<PlayerEntity> entities = databaseController.loadPlayers();
        assertTrue(entities.contains(new PlayerEntity("test1", 2)));
    }

    private void insertTwoPlayersIntoDB() {
        try (Connection connection = DriverManager.getConnection(DatabaseController.DATABASE);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("insert into %s (name, score) values (\'test1\', 1);", TABLE_NAME));
            statement.executeUpdate(String.format("insert into %s (name, score) values (\'test2\', 1);", TABLE_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}