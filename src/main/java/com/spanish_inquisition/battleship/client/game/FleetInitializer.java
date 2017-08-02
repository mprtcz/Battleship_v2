package com.spanish_inquisition.battleship.client.game;

import com.spanish_inquisition.battleship.client.board.boardcontroller.BoardController;
import com.spanish_inquisition.battleship.client.board.BoardTile;
import com.spanish_inquisition.battleship.common.Styles;

import java.util.List;
import java.util.Map;

public class FleetInitializer {

    private BoardController boardController;
    private Map<Integer, BoardTile> boardTiles;
    private List<Integer> shipPlaces;

    public List<Integer> getShipPlaces() {
        return shipPlaces;
    }

    public FleetInitializer(BoardController boardController) {
        this.boardController = boardController;
    }

    public void setUpShips() {
        boardTiles = boardController.getBoardsIndexTiles();
        shipPlaces = Fleet.getRandomFleet().getShipPlaces();
        shipPlaces.forEach(this::changeTileStyle);
    }

    private void changeTileStyle(int tileId) {
        BoardTile boardTile = boardTiles.get(tileId);
        boardTile.setStyle(Styles.SHIP_PLACED_COLOR);
    }
}