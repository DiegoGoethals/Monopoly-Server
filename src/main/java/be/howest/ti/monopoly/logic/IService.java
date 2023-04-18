package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;

import java.util.List;
import java.util.Set;

public interface IService {
    String getVersion();

    List<Tile> getTiles();

    Tile getTile(int position);

    Tile getTile(String name);

    List<String> getCommunity();

    List<String> getChance();

    Game createGame(String prefix, int numberOfPlayers);

    List<Game> getGames();

    List<Game> getGames(int players);

    List<Game> getGames(String prefix);

    List<Game> getGames(boolean started);

    void joinGame(String playerName, String gameId);

    List<Game> deleteAllGames();

    Game getGame(String gameId);

    Game rollDice(String gameId, String playerName);

    Game goBankrupt(String gameId, String playerName);

    BuyStatus buyProperty(String gameId, String playerName, String propertyName);

    BuyStatus dontBuyProperty(String gameId, String playerName, String propertyName);

    Game takeMortgage(String gameId, String playerName, String propertyName);

    Game settleMortgage(String gameId, String playerName, String propertyName);

    void getOutOfJailFine(String gameId, String playerName);

    void getOutOfJailFree(String gameId, String playerName);

    void buyHouse(String gameId, String playerName, String propertyName);

    void sellHouse(String gameId, String playerName, String propertyName);

    void buyHotel(String gameId, String playerName, String propertyName);

    void sellHotel(String gameId, String playerName, String propertyName);

    List<Game> clearGames();
}
