package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int currentTile;
    private boolean jailed;
    private int money;
    private boolean bankrupt;
    private int getOutOfJailFreeCards;
    private String taxSystem;
    private List<Property> properties;
    private int turnsInJail;
    int totalWorth;


    public Player(String name) {
        this.name = name;
        currentTile = 0;
        jailed = false;
        money = 1500;
        bankrupt = false;
        getOutOfJailFreeCards = 0;
        taxSystem = "ESTIMATE";
        properties = new ArrayList<>();
        turnsInJail = 0;
        totalWorth = 0;

    }

    public void takeMortgage(String propertyName) {
        Property property = null;
        for (Property p : properties) {
            if (p.getName().equals(propertyName)) {
                property = p;
            }
        }
        addMoney(property.getMortgage());
        property.takeMortgage();
    }

    public void settleMortgage(String propertyName) {
        Property property = null;
        for (Property p : properties) {
            if (p.getName().equals(propertyName)) {
                property = p;
            }
        }
        pay(property.getMortgage());
        property.settleMortgage();
    }

    public void move(int roll) {
        if (jailed) {
            turnsInJail++;
        }
        if (turnsInJail == 0 || turnsInJail == 3) {
            jailed = false;
            turnsInJail = 0;
            currentTile += roll;
            currentTile = currentTile % 40;
        }
    }

    public void goToJail() {
        jailed = true;
        currentTile = 10;
    }

    public void goBankrupt() {
        bankrupt = true;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public void pay(int amount) {
        money -= amount;
    }

    public void addJailCard() {
        getOutOfJailFreeCards++;
    }

    public void buyProperty(Property property) {
        pay(property.getCost());
        property.setOwner(this);
        properties.add(property);
    }

    public void setCurrentTile(int tile) {
        currentTile = tile;
    }

    public String getName() {
        return name;
    }

    public int getCurrentTile() {
        return currentTile;
    }

    public boolean isJailed() {
        return jailed;
    }

    public int getMoney() {
        return money;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public int getGetOutOfJailFreeCards() {
        return getOutOfJailFreeCards;
    }

    public void useGetOutOfJailFreeCards() {
        getOutOfJailFreeCards--;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setFree() {
        jailed = false;
    }

    public List<Property> getAutoshopProperties() {
        List<Property> autoshops = new ArrayList<>();
        for (Property property : properties) {
            if (property.getType().equals("Autoshop")) {
                autoshops.add(property);
            }
        }
        return autoshops;
    }
}
