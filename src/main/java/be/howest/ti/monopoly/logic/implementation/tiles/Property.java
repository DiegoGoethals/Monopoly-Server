package be.howest.ti.monopoly.logic.implementation.tiles;

import be.howest.ti.monopoly.logic.implementation.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Property extends Tile {

    private int cost;
    private int rent;
    private int groupSize;
    private String color;
    private boolean mortgaged;
    private Player owner;

    public Property(int position, String property, String type, int cost, int rent, int groupSize, String color) {
        super(position, property, type);
        this.cost = cost;
        this.rent = rent;
        this.groupSize = groupSize;
        this.color = color;
        mortgaged = false;
        owner = null;
    }

    public void setOwner(Player player) {
        owner = player;
    }

    public void takeMortgage() {
        mortgaged = true;
    }

    public void settleMortgage() {
        mortgaged = false;
    }

    public boolean isOwned() {
        return owner != null;
    }

    public int getMortgage() {
        return cost/2;
    }

    public int getCost() {
        return cost;
    }

    public int getRent() {
        return rent;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public String getColor() {
        return color;
    }

    public boolean isMortgaged() {
        return mortgaged;
    }

    @JsonIgnore
    public Player getOwner() {
        return owner;
    }
}
