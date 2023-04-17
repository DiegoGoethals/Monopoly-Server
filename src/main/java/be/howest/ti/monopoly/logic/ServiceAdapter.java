package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;

import java.util.List;

public class ServiceAdapter implements IService {

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tile> getTiles() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game createGame(String prefix, int numberOfPlayers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tile getTile(int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Game> getGames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tile getTile(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Game> getGames(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getCommunity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getChance() { throw new UnsupportedOperationException(); }

    @Override
    public List<Game> getGames(int players) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Game> getGames(boolean started) {
        throw new UnsupportedOperationException();
    }

    public void joinGame(String playerName, String gameId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Game> deleteAllGames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game getGame(String gameId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game rollDice(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game goBankrupt(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BuyStatus buyProperty(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BuyStatus dontBuyProperty(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game takeMortgage(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Game settleMortgage(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getOutOfJailFine(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getOutOfJailFree(String gameId, String playerName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void buyHouse(String gameId, String playerName, String propertyName) {
        throw new UnsupportedOperationException();
    }

  @Override
  public void sellHouse(String gameId, String playerName, String propertyName) {
    throw new UnsupportedOperationException();
  }
}
