package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.IService;
import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;
import be.howest.ti.monopoly.logic.implementation.turnmanagement.BuyStatus;

import java.util.List;
import java.util.Set;


public class TestService implements IService {

    IService delegate = new ServiceAdapter();

    void setDelegate(IService delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getVersion() {
        return delegate.getVersion();
    }

    @Override
    public List<Tile> getTiles() {
        return delegate.getTiles();
    }

    @Override
    public Tile getTile(int position) {
        return delegate.getTile(position);
    }

    @Override
    public Tile getTile(String name) {return delegate.getTile(name);}

    @Override
    public List<String> getChance() {return delegate.getChance();}

    @Override
    public List<String> getCommunity() {
        return delegate.getCommunity();
    }

    public Game createGame(String prefix, int numberOfPlayers) {
        return delegate.createGame(prefix, numberOfPlayers);
    }

    @Override
    public List<Game> getGames() {
        return delegate.getGames();
    }

    @Override
    public List<Game> getGames(int players) {
        return delegate.getGames(players);
    }

    @Override
    public List<Game> getGames(String prefix) {
        return delegate.getGames(prefix);
    }

    @Override
    public List<Game> getGames(boolean started) {
        return delegate.getGames(started);
    }

    @Override
    public void joinGame(String playerName, String gameId) {
        delegate.joinGame(playerName, gameId);
    }

    @Override
    public List<Game> deleteAllGames() {
        return delegate.deleteAllGames();
    }

    @Override
    public Game getGame(String gameId) {
        return delegate.getGame(gameId);
    }

    @Override
    public Game rollDice(String gameId, String playerName) {
        return delegate.rollDice(gameId, playerName);
    }

    @Override
    public Game goBankrupt(String gameId, String playerName) {
        return delegate.goBankrupt(gameId, playerName);
    }

    @Override
    public BuyStatus buyProperty(String gameId, String playerName, String propertyName) {
        return delegate.buyProperty(gameId, playerName, propertyName);
    }

    @Override
    public BuyStatus dontBuyProperty(String gameId, String playerName, String propertyName) {
        return delegate.dontBuyProperty(gameId, playerName, propertyName);
    }

    @Override
    public Game takeMortgage(String gameId, String playerName, String propertyName) {
        return delegate.takeMortgage(gameId, playerName, propertyName);
    }

    @Override
    public Game settleMortgage(String gameId, String playerName, String propertyName) {
        return delegate.settleMortgage(gameId, playerName, propertyName);
    }

    @Override
    public void getOutOfJailFine(String gameId, String playerName) {
        delegate.getOutOfJailFine(gameId, playerName);
    }

    @Override
    public void getOutOfJailFree(String gameId, String playerName) {
        delegate.getOutOfJailFree(gameId, playerName);
    }

    @Override
    public Set<Auction> getBankAuctions(String gameId) { return delegate.getBankAuctions(gameId); }

    @Override
    public void placeBidOnBankAuction(String gameId, String propertyName, String p, int bid) {
        delegate.placeBidOnBankAuction(gameId, propertyName, p, bid);
    }

    @Override
    public Set<Auction> getPlayerAuctions(String gameId, String playerName) {
        return delegate.getPlayerAuctions(gameId, playerName);
    }

    @Override
    public void startPlayerAuction(String gameId, String playerName, String propertyName, int duration, int startBid) {
        delegate.startPlayerAuction(gameId, playerName, propertyName, duration, startBid);
    }

    @Override
    public void placeBidOnPlayerAuction(String gameId, String auctionStarter, String propertyName, String bidder, int bid) {
        delegate.placeBidOnPlayerAuction(gameId, auctionStarter, propertyName, bidder, bid);
    }

    @Override
    public void trade(String gameId, String playerName, String propertyName, int money, String tradePartnerName, String returnPropertyName,
                      int returnMoney) {
        delegate.trade(gameId, playerName, propertyName, money, tradePartnerName, returnPropertyName, returnMoney);
    }

    public void useEstimateTax(String gameId, String playerName) {
        delegate.useEstimateTax(gameId, playerName);
    }

    @Override
    public void useComputeTax(String gameId, String playerName) {
        delegate.useComputeTax(gameId, playerName);
    }

    @Override
    public BuyStatus collectRent(String gameId, String playerName, String propertyName, String debtorName) {
        return delegate.collectRent(gameId, playerName, propertyName, debtorName);
    }
}
