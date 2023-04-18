package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.*;
import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.implementation.tiles.Street;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;

import java.util.List;

import java.util.ArrayList;
import java.util.Set;


public class MonopolyService extends ServiceAdapter {

    private GameManager games;

    public MonopolyService() {
        games = new GameManager();
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public List<Tile> getTiles() {
        return List.of(
                new Tile(0, "Go", "Go"),
                new Street(1, "Mazda MX-5", "Street", 60, 2, 2, "BROWN", 10,
                        30, 90, 160, 250, 50),
                new Tile(2, "Community Chest I", "Community"),
                new Street(3, "Mazda RX-7", "Street", 60, 4, 2, "BROWN", 20,
                        60, 180, 320, 450, 50),
                new Tile(4, "Income Tax", "Tax"),
                new Property(5, "Smokey Nagatas Autoshop", "Autoshop", 200, 25, 4, "BLACK"),
                new Street(6, "Ford Focus", "Street", 100, 6, 3, "LIGHTBLUE", 30,
                        90, 270, 400, 550, 50),
                new Tile(7, "Chance I", "Chance"),
                new Street(8, "Ford Mustang", "Street", 100, 6, 3, "LIGHTBLUE", 30,
                        90, 270, 400, 550, 50),
                new Street(9, "Ford GT", "Street", 120, 8, 3, "LIGHTBLUE", 40,
                        100, 300, 450, 600, 50),
                new Tile(10, "Impound Lot", "Impound"),
                new Street(11, "Dodge Challenger", "Street", 140, 10, 3, "VIOLET", 50,
                        150, 450, 625, 750, 100),
                new Property(12, "Electric Company", "Utility", 150, 0, 2, "WHITE"),
                new Street(13, "Dodge Charger", "Street", 140, 10, 3, "VIOLET", 50,
                        150, 450, 625, 750, 100),
                new Street(14, "Dodge Hellcat", "Street", 160, 12, 3, "VIOLET", 60,
                        180, 500, 700, 900, 100),
                new Property(15, "Rocket Bunny Autoshop", "Autoshop", 200, 25, 4, "BLACK"),
                new Street(16, "Nissan 370Z", "Street", 180, 14, 3, "ORANGE", 70,
                        200, 550, 700, 900, 100),
                new Tile(17, "Community Chest II", "Community"),
                new Street(18, "Nissan Datsun 240Z", "Street", 180, 14, 3, "ORANGE", 70,
                        200, 550, 700, 950, 100),
                new Street(19, "Nissan GTR R35 Nismo", "Street", 200, 16, 3, "ORANGE", 80,
                        220, 600, 800, 1000, 100),
                new Tile(20, "Free Parking", "Free Parking"),
                new Street(21, "Ferrari FXX", "Street", 220, 18, 3, "RED", 90,
                        250, 700, 875, 1050, 150),
                new Tile(22, "Chance II", "chance"),
                new Street(23, "Ferrari 488 GTB", "Street", 220, 18, 3, "RED", 90,
                        250, 700, 875, 1050, 150),
                new Street(24, "Ferrari Laferrari", "Street", 240, 20, 3, "RED", 100,
                        300, 750, 925, 1100, 150),
                new Property(25, "Fujiwara Autoshop", "Autoshop", 200, 25, 4, "BLACK"),
                new Street(26, "Aston Martin DB9", "Street", 260, 22, 3, "YELLOW", 110,
                        330, 800, 975, 1150, 150),
                new Street(27, "Aston Martin Zagato V12", "Street", 260, 22, 3, "YELLOW", 110,
                        330, 800, 975, 1150, 150),
                new Property(28, "Water Works", "Utility", 150, 0, 2, "WHITE"),
                new Street(29, "Aston Martin Victor", "Street", 280, 24, 3, "YELLOW", 120,
                        360, 850, 1025, 1200, 150),
                new Tile(30, "Your car has been impounded", "Impounded"),
                new Street(31, "Lamborghini Urus", "Street", 300, 26, 3, "GREEN", 130,
                        390, 900, 1100, 1275, 200),
                new Street(32, "Lamborghini Countach", "Street", 300, 26, 3, "GREEN", 130,
                        390, 900, 1100, 1275, 200),
                new Tile(33, "Community Chest III", "Community"),
                new Street(34, "Lamborghini Aventador", "Street", 320, 28, 3, "GREEN", 150,
                        450, 1000, 1200, 1400, 200),
                new Property(35, "Radiator Springs Autoshop", "Autoshop", 200, 25, 4, "BLACK"),
                new Tile(36, "Chance III", "Chance"),
                new Street(37, "Bentley Bentayga", "Street", 350, 35, 2, "DARKBLUE", 175,
                        500, 1100, 1300, 1500, 200),
                new Tile(38, "Luxury Tax", "Tax"),
                new Street(39, "Bentley Continental GT", "Street", 400, 50, 2, "DARKBLUE", 200,
                        600, 1400, 1700, 2000, 200)
        );
    }

    @Override
    public Tile getTile(int index) {
        for (Tile tile : getTiles()) {
            if (tile.getPosition() == index) {
                return tile;
            }
        } return null;
    }

    @Override
    public Tile getTile(String name) {
        for (Tile tile : getTiles()) {
            if (tile.getName().equals(name)) {
                return tile;
            }
        } return null;
    }

    @Override
    public List<String> getCommunity() {
        List<String> communityCards = new ArrayList<>();
        communityCards.add("Advance to Start (Collect $200)");
        communityCards.add("Bank error in your favor. Collect $200");
        communityCards.add("You have been caught drunk driving. Pay $50");
        communityCards.add("From selling some car parts you get $50");
        communityCards.add("Get out of jail free");
        communityCards.add("Go to jail. Go directly to jail, do not pass Start, do not collect $200");
        communityCards.add("You have won a cleanest car contest. Receive $100");
        communityCards.add("You have cleaned the neighbors' car. Collect $20");
        communityCards.add("You parked illegally. Pay $10");
        communityCards.add("You rented a circuit. Pay $100");
        communityCards.add("You bought a spoiler for $100");
        communityCards.add("Speeding ticket. Pay $50");
        communityCards.add("Pay $25 for fixing minor scratches");
        communityCards.add("You installed illegal car modifications. Pay $40 fee per visual mod and $115 per performance mod");
        communityCards.add("You have lost a 10mm socket. Pay $15");
        communityCards.add("You have won a street race. You win $100");

        return communityCards;
    }

    public List<String> getChance() {
        List<String> chanceCards = new ArrayList<>();
        chanceCards.add("Advance to Dodge Charger");
        chanceCards.add("Advance to Start (collect $200)");
        chanceCards.add("Advance to Mazda RX-7 FC. If you pass start, collect $200");
        chanceCards.add("Advance to Ford GT. If you pass Start, collect $200");
        chanceCards.add("Advance to the nearest Autoshop. If unowned, you can buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled.");
        chanceCards.add("Advance to the nearest Autoshop. If unowned, you can buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled.");
        chanceCards.add("Advance to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay ten times the amount thrown.");
        chanceCards.add("Bank pays you dividend of $50.");
        chanceCards.add("Get out of jail free.");
        chanceCards.add("Go back 3 spaces");
        chanceCards.add("Go to Jail. Go directly to Jail, do not pass Start, do not collect $200.");
        chanceCards.add("Make general repairs on all your cars. For each visual part, pay $25. For each performance part, pay $100.");
        chanceCards.add("Speeding ticket $15.");
        chanceCards.add("Take a trip to Fujiwara Autoshop. If you pass Start, collect $200.");
        chanceCards.add("Make sure no-one leaks the street race location to the police. Pay each player $50.");
        chanceCards.add("Your betting on an F1 race was correct. Collect $150.");
        return chanceCards;
    }

    public Game createGame(String prefix, int numberOfPlayers) {
        Game game = new Game(prefix, numberOfPlayers, getTiles());
        games.addGame(game);
        return game;
    }

    @Override
    public List<Game> getGames() {
        return games.getGameStorage();
    }

    @Override
    public void joinGame(String playerName, String gameId) {
        Player player = new Player(playerName);
        games.joinGame(player, gameId);
    }

    @Override
    public Game getGame(String gameId) {
        return games.getGameStorage().get(games.getGameIndex(gameId));
    }

    @Override
    public Game rollDice(String gameId, String playerName) {
        return games.rollDice(gameId, playerName, getChance(), getCommunity());
    }

    @Override
    public Game goBankrupt(String gameId, String playerName) {
        return games.goBankrupt(gameId, playerName);
    }

    @Override
    public BuyStatus buyProperty(String gameId, String playerName, String propertyName) {
        return games.buyProperty(gameId, playerName, propertyName);
    }

    @Override
    public BuyStatus dontBuyProperty(String gameId, String playerName, String propertyName) {
        return games.dontBuyProperty(gameId, playerName, propertyName);
    }

    @Override
    public Game takeMortgage(String gameId, String playerName, String propertyName) {
        return games.takeMortgage(gameId, playerName, propertyName);
    }

    @Override
    public Game settleMortgage(String gameId, String playerName, String propertyName) {
        return games.settleMortgage(gameId, playerName, propertyName);
    }

    @Override
    public void getOutOfJailFine(String gameId, String playerName) {
        games.getOutOfJailFine(gameId, playerName);
    }

    @Override
    public void getOutOfJailFree(String gameId, String playerName) {
        games.getOutOfJailFree(gameId, playerName);
    }

    @Override
    public void buyHouse(String gameId, String playerName, String propertyName) {
        games.buyHouse(gameId, playerName, propertyName);
    }

  @Override
  public void sellHouse(String gameId, String playerName, String propertyName) {
        games.sellHouse(gameId, playerName, propertyName);
  }

  @Override
  public void buyHotel(String gameId, String playerName, String propertyName) {
        games.buyHotel(gameId, playerName, propertyName);
  }

  @Override
  public void sellHotel(String gameId, String playerName, String propertyName) {
        games.sellHotel(gameId, playerName, propertyName);
  }

  @Override
  public List<Game> clearGames() {
        return games.clearGames();
  }
}
