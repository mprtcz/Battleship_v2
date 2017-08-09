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

    public PlayerEntity(String name, int score) {
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

    public String getReadyToSendRepresentation() {
        return name + " = " + score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerEntity that = (PlayerEntity) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
