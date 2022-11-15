package be.howest.ti.monopoly.logic.implementation.turnmanagement;

import be.howest.ti.monopoly.logic.implementation.tiles.Tile;

public class Move {

    private Tile tile;
    private String description;

    public Move(Tile tile, String description) {
        this.tile = tile;
        this.description = description;
    }

    public Tile seeTile() {
        return tile;
    }

    public String getTile() {
        return tile.getName();
    }

    public String getDescription() {
        return description;
    }
}
