package com.spanish_inquisition.battleship.client.game;

import com.spanish_inquisition.battleship.client.board.boardcontroller.OpponentBoardController;
import com.spanish_inquisition.battleship.client.board.boardcontroller.PlayerBoardController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ListView;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.*;

/**
 * @author Michal_Partacz
 */
@Test
public class GameTest {
    @Test
    public void testDisplayScore() throws Exception {
        Game game = new Game();
        ListView<String> listView = new ListView();
        game.setScoreListView(listView);
        String testString = "Score : 1";
        game.displayScore(testString);
        assertEquals(listView.getItems().size(), 1);
        assertTrue(listView.getItems().get(0).equals(testString));
    }

    @BeforeSuite
    public void setupJavaFx() {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });
    }

    @Test
    public void shouldBuildPlayersBoard() {
        Game game = new Game();
        final PlayerBoardController boardController = mock(PlayerBoardController.class);
        assertNull(game.playerBoardController);
        game.buildPlayersBoard(boardController);
        assertNotNull(game.playerBoardController);
    }

    @Test
    public void shouldBuildOpponentsBoard() {
        Game game = new Game();
        final OpponentBoardController boardController = mock(OpponentBoardController.class);
        assertNull(game.opponentBoardController);
        game.buildOpponentsBoard(boardController);
        assertNotNull(game.opponentBoardController);
    }
}