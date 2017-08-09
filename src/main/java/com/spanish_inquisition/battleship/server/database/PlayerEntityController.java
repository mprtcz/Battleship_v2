package com.spanish_inquisition.battleship.server.database;

import com.spanish_inquisition.battleship.server.Player;

import java.util.List;

/**
 * Created by michal on 08.08.17.
 */
public class PlayerEntityController {
    private PlayerEntity playerEntity;
    private Player player;
    private DatabaseController databaseController = new DatabaseController();

    public PlayerEntityController(Player player) {
        this.player = player;
        playerEntity = new PlayerEntity(player.getName());
    }

    public PlayerEntityController(Player player, DatabaseController databaseController) {
        this.player = player;
        this.databaseController = databaseController;
        playerEntity = new PlayerEntity(player.getName());
    }

    public void incrementEntityScore() {
        playerEntity.incrementScore();
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public Player getPlayer() {
        return player;
    }

    public void saveOrUpdatePlayerInDB() {
        databaseController.saveOrUpdatePlayerEntity(playerEntity);
    }

    public List<PlayerEntity> getPlayersFromDB() {
        return databaseController.loadPlayers();
    }
}
