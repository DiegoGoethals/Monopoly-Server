package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private List<Game> games;

    public GameManager() {
        games = new ArrayList<>();
    }

    public List<Game> getGameStorage() {
        return games;
    }

    public int getGameIndex(String gameId) {
        return Integer.parseInt(gameId.substring(8));
    }

    private Game findGame(String gameId){
        int gameIndex = getGameIndex(gameId);
        return games.get(gameIndex);
    }

    public void addGame(Game game) {
        String id = Integer.toString(games.size());
        game.setId(id);
        games.add(game);
    }

    public void joinGame(Player player, String gameId) {
        int gameIndex = getGameIndex(gameId);
        if (!games.get(gameIndex).isStarted()) {
          games.get(gameIndex).getPlayers().add(player);
          if (games.get(gameIndex).getNumberOfPlayers() == games.get(gameIndex).getPlayers().size()) {
            games.get(gameIndex).startGame();
          }
        }
    }

    public Game rollDice(String gameId, String playerName, List<String> chance, List<String> community) {
        Game game = findGame(gameId);
        game.processTurn(playerName, chance, community);
        return game;
    }

    public Game goBankrupt(String gameId, String playerName) {
        Game game = findGame(gameId);
        game.goBankrupt(playerName);
        return game;
    }

    public BuyStatus buyProperty(String gameId, String playerName, String propertyName) {
        Game game = findGame(gameId);
        return game.buyProperty(playerName, propertyName);
    }

    public BuyStatus dontBuyProperty(String gameId, String playerName, String propertyName) {
        Game game = findGame(gameId);
        return game.dontBuyProperty(playerName, propertyName);
    }

    public Game takeMortgage(String gameId, String playerName, String propertyName) {
        Game game = findGame(gameId);
        return game.takeMortgage(playerName, propertyName);
    }

    public Game settleMortgage(String gameId, String playerName, String propertyName) {
        Game game = findGame(gameId);
        return game.settleMortgage(playerName, propertyName);
    }

    public void getOutOfJailFine(String gameId, String playerName) {
        Game game = findGame(gameId);
        game.getOutOfJailFine(playerName);
    }

    public void getOutOfJailFree(String gameId, String playerName) {
        Game game = findGame(gameId);
        game.getOutOfJailFree(playerName);
    }

    public void buyHouse(String gameId, String playerName, String propertyName) {
        Game game = findGame(gameId);
        game.buyHouse(playerName, propertyName);
    }

    public void sellHouse(String gameId, String playerName, String propertyName) {
        Game game = findGame(gameId);
        game.sellHouse(playerName, propertyName);
    }
}
