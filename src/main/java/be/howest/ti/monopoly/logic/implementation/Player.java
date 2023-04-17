package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.implementation.tiles.Street;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {

    private final String name;
    private int currentTile;
    private boolean jailed;
    private int money;
    private boolean bankrupt;
    private int getOutOfJailFreeCards;
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
        properties = new ArrayList<>();
        turnsInJail = 0;
        totalWorth = 0;

    }

    public void takeMortgage(String propertyName) {
        Property property = findPropertyByName(propertyName);
        if (!property.isMortgaged()) {
          addMoney(property.getMortgage());
          property.takeMortgage();
        }
    }

    public void settleMortgage(String propertyName) {
        Property property = findPropertyByName(propertyName);
        if (property.isMortgaged()) {
          pay(property.getMortgage());
          property.settleMortgage();
      }
    }

    public void move(int roll) {
        if (jailed) {
            turnsInJail++;
        }
        if (turnsInJail == 0 || turnsInJail == 3) {
            jailed = false;
            turnsInJail = 0;
            currentTile += roll;
            currentTile %= 40;
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

  public Property findPropertyByName(String propertyName) {
    return properties.stream().filter(p -> p.getName().equals(propertyName)).findFirst().orElse(null);
  }

    public List<Property> getAutoshopProperties() {
      return properties.stream().filter(p -> p.getType().equals("Autoshop"))
        .collect(Collectors.toList());
    }

    public void buyHouse(String propertyName) {
      Property property = findPropertyByName(propertyName);
      if (property instanceof Street) {
        Street street = (Street) property;
        street.buyHouse();
        pay(street.getHousePrice());
      } else {
        throw new IllegalArgumentException("Property is not a street");
      }
    }
}
