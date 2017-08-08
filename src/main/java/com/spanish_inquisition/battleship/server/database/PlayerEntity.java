package com.spanish_inquisition.battleship.server.database;

/**
 * Created by michal on 08.08.17.
 */
public class PlayerEntity {
    private int id;
    private String name;
    private int score;

    public PlayerEntity(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public PlayerEntity(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incrementScore() {
        this.score++;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
