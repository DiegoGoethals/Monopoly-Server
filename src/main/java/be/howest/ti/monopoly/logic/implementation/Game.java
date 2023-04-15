package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.Move;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.Turn;

import java.util.*;

public class Game {

    private String id;
    private final int numberOfPlayers;
    private boolean started;
    private ArrayList<Player> players;
    private ArrayList<Turn> turns;
    private int[] lastDiceRoll;
    private boolean canRoll;
    private boolean ended;
    private int currentPlayer;
    private String winner;
    private int amountOfDoubleRolls;
    private ArrayList<Player> bankruptPlayers;
    private final int jailFine;
    private int taxAmount;
    private final List<Tile> tiles;

    public Game(String prefix, int numberOfPlayers, List<Tile> tiles) {
        id = prefix;
        this.numberOfPlayers = numberOfPlayers;
        started = false;
        players = new ArrayList<>();
        turns = new ArrayList<>();
        lastDiceRoll = new int[2];
        canRoll = true;
        ended = false;
        currentPlayer = 0;
        winner = null;
        amountOfDoubleRolls = 0;
        bankruptPlayers = new ArrayList<>();
        jailFine = 50;
        taxAmount = 200;
        this.tiles = tiles;
    }

    public void startGame() {
        started = true;
        currentPlayer = new Random().nextInt(players.size());
    }

    public void setId(String id) {
        this.id += "_" + id;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isStarted() {
        return started;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public int[] getLastDiceRoll() {
        return lastDiceRoll;
    }

    public boolean isCanRoll() {
        return canRoll;
    }

    public boolean isEnded() {
        return ended;
    }

    public String getCurrentPlayer() {
        if (started) {
            return players.get(currentPlayer).getName();
        }
        return "";
    }

    public String getWinner() {
        return winner;
    }

    public int[] rollDice() {
        Random rolls = new Random();
        int[] roll = new int[2];
        roll[0] = rolls.nextInt(6) + 1;
        roll[1] = rolls.nextInt(6) + 1;
        lastDiceRoll = roll;
        return roll;
    }

    public void processTurn(String playerName, List<String> chance, List<String> community) {
        if (canRoll) {
            Player player = players.get(currentPlayer);
            if (player.getName().equals(playerName)) {
                int[] roll = rollDice();
                Turn turn = new Turn(player, roll);
                turns.add(turn);
                int lastPosition = player.getCurrentTile();
                turn.processMove(lastPosition, tiles, chance, community, this);
                Move lastMove = getLastTurn().getLastMove();
                switch (lastMove.getDescription()) {
                  case "can buy this property in direct sale":
                    canRoll = false;
                    break;
                  case "Has to pay rent":
                    payRent((Property) tiles.get(lastPosition), players.get(currentPlayer));
                    checkDoubleRoll(roll);
                    break;
                  case "Has to pay double rent":
                    payRent((Property) tiles.get(lastPosition), players.get(currentPlayer));
                    payRent((Property) tiles.get(lastPosition), players.get(currentPlayer));
                    checkDoubleRoll(roll);
                    break;
                  case "has to pay taxes":
                    players.get(currentPlayer).pay(taxAmount);
                    if (players.get(currentPlayer).getMoney() < 0) {
                        players.get(currentPlayer).goBankrupt();
                        for (Property property : players.get(currentPlayer).getProperties()) {
                            property.setOwner(null);
                        }
                        bankruptPlayers.add(players.get(currentPlayer));
                        checkWinner();
                    }
                    checkDoubleRoll(roll);
                    break;
                  default:
                    checkDoubleRoll(roll);
                    break;
                }
                checkWinner();
            }
        }
    }

    private void bankruptcy(Property property, Player player, int rent) {
        if (player.getMoney() < rent) {
            goBankruptByDebt(property.getOwner());
            checkWinner();
        }
    }

    private void payStreetRentHelper(Property property, Player player, int rent) {
        if (player.getMoney() > rent) {
            property.getOwner().addMoney(rent);
            player.pay(rent);
        }
    }

    public void payStreetRent(Property property, Player player) {
      if (property.getHouseCount() == 0 && property.getHotelCount() == 0) {
        bankruptcy(property, player, property.getRent());
        payStreetRentHelper(property, player, property.getRent());
      } else if (property.getHouseCount() == 1) {
        bankruptcy(property, player, property.getRentWithOneHouse());
        payStreetRentHelper(property, player, property.getRentWithOneHouse());
      } else if (property.getHouseCount() == 2) {
        bankruptcy(property, player, property.getRentWithTwoHouses());
        payStreetRentHelper(property, player, property.getRentWithTwoHouses());
      } else if (property.getHouseCount() == 3) {
        bankruptcy(property, player, property.getRentWithThreeHouses());
        payStreetRentHelper(property, player, property.getRentWithThreeHouses());
      } else if (property.getHouseCount() == 4) {
        bankruptcy(property, player, property.getRentWithFourHouses());
        payStreetRentHelper(property, player, property.getRentWithFourHouses());
      } else if (property.getHotelCount() == 1) {
        bankruptcy(property, player, property.getRentWithHotel());
        payStreetRentHelper(property, player, property.getRentWithHotel());
      }
    }

    public void payAutoShopRentHelper(List<Property> ownedShops, int ownedShopsAmount, Player player, Player owner, int rent) {
        if (ownedShops.size() == ownedShopsAmount) {
            if (player.getMoney() > rent) {
                owner.addMoney(rent);
                player.pay(rent);
            } else {
                goBankruptByDebt(owner);
            }
        }
    }

    public void payAutoShopRent(Property property, Player player) {
        Player owner = property.getOwner();
        List<Property> ownedShops = owner.getAutoshopProperties();
        payAutoShopRentHelper(ownedShops, 4, player, owner, 200);
        payAutoShopRentHelper(ownedShops, 3, player, owner, 100);
        payAutoShopRentHelper(ownedShops, 2, player, owner, 50);
        payAutoShopRentHelper(ownedShops, 1, player, owner, 25);
    }

    public void payUtilityRent(Property property, Player player) {
        Player owner = property.getOwner();
        ArrayList<Property> owned = new ArrayList<>();
        for (Property ownedProperty : owner.getProperties()) {
            if (ownedProperty.getType().equals("Utility")) {
                owned.add(ownedProperty);
            }
        }
        if (owned.size() == 2) {
            payBasedByRoll(property, player, 10);
        }
        else {
            payBasedByRoll(property, player, 4);
        }
    }

    private void payBasedByRoll(Property property, Player player, int multiplyByNumber) {
        Player owner = property.getOwner();
        int rentBasedByRoll = lastDiceRoll[0] + lastDiceRoll[1];
        if (player.getMoney() > rentBasedByRoll * multiplyByNumber) {
            owner.addMoney(rentBasedByRoll * multiplyByNumber);
            player.pay(rentBasedByRoll * multiplyByNumber);
        } else {
            goBankruptByDebt(owner);}
    }

    public void payRent(Property property, Player debtor) {
        if (!property.isMortgaged()) {
            if (property.getType().equals("Street")) {
                payStreetRent(property, debtor);
            } else if (property.getType().equals("Autoshop")) {
                payAutoShopRent(property, debtor);
            } else {
                payUtilityRent(property, debtor);
            }
        }
    }

    public void goBankruptByDebt(Player other) {
        Player player = players.get(currentPlayer);
        other.addMoney(player.getMoney());
        player.pay(player.getMoney());
        for (Property ownedProperty : player.getProperties()) {
            other.getProperties().add(ownedProperty);
            ownedProperty.setOwner(other);
        }
        player.getProperties().clear();
        player.goBankrupt();
        bankruptPlayers.add(player);
        checkWinner();
    }

    public void checkDoubleRoll(int[] roll) {
        if (roll[0] == roll[1] && amountOfDoubleRolls < 3) {
            amountOfDoubleRolls++;
        } else {
            amountOfDoubleRolls = 0;
            changeCurrentPlayer();
        }
    }

    public void goBankrupt(String playerName) {
        Player player = findPlayerByName(playerName);
        player.goBankrupt();
        bankruptPlayers.add(player);
        checkWinner();
    }

    public void changeCurrentPlayer() {
        currentPlayer++;
        currentPlayer %= players.size();
        while (bankruptPlayers.contains(players.get(currentPlayer))) {
            currentPlayer++;
            currentPlayer %= players.size();
        }
    }

    public void checkWinner() {
        if (bankruptPlayers.size() == players.size() - 1) {
          winner = players.stream().filter(player -> !bankruptPlayers.contains(player)).findFirst().get().getName();
          canRoll = false;
          ended = true;
        }
    }

    public BuyStatus buyProperty(String playerName, String propertyName) {
        Move lastMove = getLastTurn().getLastMove();
        if (!canRoll && lastMove.getDescription().equals("can buy this property in direct sale")) {
            Player player = players.get(currentPlayer);
            Property propertyToBuy = null;
            boolean bought = false;
            if (player.getName().equals(playerName)) {
                propertyToBuy = (Property) tiles.get(player.getCurrentTile());
                if (player.getMoney() > propertyToBuy.getCost()) {
                    player.buyProperty(propertyToBuy);
                    bought = true;
                }
            }
            canRoll = true;
            checkDoubleRoll(lastDiceRoll);
            if (bought) {
                getLastTurn().getMoves().add(new Move(propertyToBuy, "Property bought successfully"));
                return new BuyStatus(propertyToBuy.getName(), "Property bought successfully");
            } else {
                getLastTurn().getMoves().add(new Move(propertyToBuy, "Property not bought because of no money"));
                return new BuyStatus(propertyToBuy.getName(), "You don't have enough money to buy this property");
            }
        }
        return null;
    }

    public Turn getLastTurn() {
        if (turns.size() > 0) {
            return turns.get(turns.size() - 1);
        } else {
            return null;
        }
    }

    public BuyStatus dontBuyProperty(String playerName, String propertyName) {
        Move lastMove = getLastTurn().getLastMove();
        if (!canRoll && lastMove.getDescription().equals("can buy this property in direct sale")) {
            Player player = players.get(currentPlayer);
            if (player.getName().equals(playerName)) {
                Property propertyToBuy = (Property) tiles.get(player.getCurrentTile());
                canRoll = true;
                checkDoubleRoll(lastDiceRoll);
                getLastTurn().getMoves().add(new Move(propertyToBuy, "You didn't buy this property"));
                return new BuyStatus(propertyToBuy.getName(), "You decided to not buy this property");
            }
        }
        return null;
    }

    public Game takeMortgage(String playerName, String propertyName) {
        Player player = findPlayerByName(playerName);
        if (player == players.get(currentPlayer)) {
            player.takeMortgage(propertyName);
        }
        return this;
    }

    public Game settleMortgage(String playerName, String propertyName) {
        Player player = findPlayerByName(playerName);
        if (player == players.get(currentPlayer)) {
            player.settleMortgage(propertyName);
        }
        return this;
    }

    public Player findPlayerByName(String playerName) {
        return players.stream().filter(player -> player.getName().equals(playerName)).findFirst().orElse(null);
    }

    public void getOutOfJailFine(String playerName) {
        Player p = findPlayerByName(playerName);
        if (p.isJailed()) {
            p.pay(jailFine);
            p.setFree();
        }
    }

    public void getOutOfJailFree(String playerName) {
        Player p = findPlayerByName(playerName);
        if (p.isJailed() && p.getGetOutOfJailFreeCards() > 0) {
            p.useGetOutOfJailFreeCards();
            p.setFree();
        }
    }
}
