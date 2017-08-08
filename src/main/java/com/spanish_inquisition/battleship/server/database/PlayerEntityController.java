package com.spanish_inquisition.battleship.server.database;

import com.spanish_inquisition.battleship.server.Player;

/**
 * Created by michal on 08.08.17.
 */
public class PlayerEntityController {
    private PlayerEntity playerEntity;
    private Player player;

    public PlayerEntityController(Player player) {
        this.player = player;
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
}
