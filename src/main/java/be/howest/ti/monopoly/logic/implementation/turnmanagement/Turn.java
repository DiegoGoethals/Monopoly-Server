package be.howest.ti.monopoly.logic.implementation.turnmanagement;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.implementation.tiles.Street;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Turn {

    private final int [] roll;
    private final Player player;
    private final String type;
    private ArrayList<Move> moves;

    public Turn(Player player, int[] roll) {
        this.roll = roll;
        this.player = player;
        type = "DEFAULT";
        moves = new ArrayList<>();
    }

    public void processMove(int lastPosition, List<Tile> tiles, List<String> chance, List<String> community, Game game) {
        player.move(roll[0] + roll[1]);
        if (player.isJailed()) {
            moves.add(new Move(tiles.get(10), "In jail"));
        }
        int currentTile = player.getCurrentTile();
        Tile location = tiles.get(currentTile);
        switch (currentTile) {
            case 0:
                moves.add(new Move(tiles.get(0), "Start"));
                break;
            case 10:
              if (!player.isJailed()) {
                moves.add(new Move(tiles.get(10), "Just visiting"));
              }
              break;
            case 20:
                moves.add(new Move(tiles.get(20), "Nothing special happened"));
                break;
            case 30:
                moves.add(new Move(tiles.get(30), "Car has been impounded"));
                player.goToJail();
                moves.add(new Move(tiles.get(10), "In jail"));
                break;
          default:
              break;
        }
        if (lastPosition >= 28 && player.getCurrentTile() <= 11 && !player.isJailed()) {
            player.addMoney(200);
        }
        if (location.getType().equals("Street") || location.getType().equals("Utility") ||
            location.getType().equals("Autoshop")) {
            Property property = ((Property) location);
            if (!property.isOwned()) {
                moves.add(new Move(location, "can buy this property in direct sale"));
            } else {
                Player owner = property.getOwner();
                if (owner != player) {
                    moves.add(new Move(location, "Has to pay rent"));
                } else {
                    moves.add(new Move(location, "Property is already yours"));
                }
            }
        }
        if (tiles.get(currentTile).getType().equals("Community") || tiles.get(currentTile).getType().equals("Chance")) {
            drawCard(tiles, tiles.get(currentTile).getType(), player, chance, community, game);
        }
        if (tiles.get(currentTile).getType().equals("Tax")) {
            moves.add(new Move(tiles.get(currentTile), "has to pay taxes"));
        }
    }

    public void drawCard(List<Tile> tiles, String tileType, Player player, List<String> chance, List<String> community, Game game) {
        String card;
        if (tileType.equals("Chance")) {
            card = chance.get(new Random().nextInt(chance.size()));
            moves.add(new Move(tiles.get(player.getCurrentTile()), card));
            switch (card) {
                case "Advance to Dodge Charger":
                    if (player.getCurrentTile() > 13) {
                        player.addMoney(200);
                    }
                    player.setCurrentTile(13);
                    break;
                case "Advance to Start (collect $200)":
                    player.setCurrentTile(0);
                    player.addMoney(200);
                    break;
                case "Advance to Mazda RX-7 FC. If you pass start, collect $200":
                    if (player.getCurrentTile() > 3) {
                        player.addMoney(200);
                    }
                    player.setCurrentTile(3);
                    break;
                case "Advance to Ford GT. If you pass Start, collect $200":
                    if (player.getCurrentTile() > 9) {
                        player.addMoney(200);
                    }
                    player.setCurrentTile(9);
                    break;
                case "Advance to the nearest Autoshop. If unowned, you can buy it from the Bank. If owned, pay owner twice the rental to which they are otherwise entitled.":
                    if (player.getCurrentTile() == 7) {
                        player.setCurrentTile(15);
                    } else if (player.getCurrentTile() == 22) {
                        player.setCurrentTile(25);
                    } else if (player.getCurrentTile() == 36) {
                        player.setCurrentTile(5);
                        player.addMoney(200);
                    }
                    Property shop = (Property) tiles.get(player.getCurrentTile());
                    if (shop.isOwned()) {
                      moves.add(new Move(shop, "has to pay double rent"));
                    } else {
                      moves.add(new Move(shop, "can buy this property in direct sale"));
                    }
                    break;
                case "Advance to the nearest Utility. If unowned, you may buy it from the Bank. If owned, throw dice and pay ten times the amount thrown.":
                    if (player.getCurrentTile() == 7) {
                        player.setCurrentTile(12);
                    } else if (player.getCurrentTile() == 22) {
                        player.setCurrentTile(28);
                    } else if (player.getCurrentTile() == 36) {
                        player.setCurrentTile(12);
                        player.addMoney(200);
                    }
                    Property utility = (Property) tiles.get(player.getCurrentTile());
                    if (utility.isOwned()) {
                        moves.add(new Move(utility, "has to pay double rent"));
                    } else {
                        moves.add(new Move(utility, "can buy this property in direct sale"));
                    }
                    break;
                case "Bank pays you dividend of $50.":
                    player.addMoney(50);
                    break;
                case "Get out of jail free.":
                    player.addJailCard();
                    break;
                case "Go back 3 spaces":
                    player.setCurrentTile(player.getCurrentTile() - 3 - roll[0] - roll[1]);
                    processMove(player.getCurrentTile(), tiles, chance, community, game);
                    break;
                case "Go to Jail. Go directly to Jail, do not pass Start, do not collect $200.":
                    player.goToJail();
                    break;
                case "Make general repairs on all your cars. For each visual part, pay $25. For each performance part, pay $100.":
                  List<Street> streetProps = player.getProperties().stream().filter(p -> p instanceof Street).map(p -> (Street) p).collect(Collectors.toList());
                  for (Street property : streetProps) {
                        player.pay(property.getHouseCount() * 25);
                        player.pay(property.getHotelCount() * 100);
                    }
                    break;
                case "Speeding ticket $15.":
                    player.pay(15);
                    break;
                case "Take a trip to Fujiwara Autoshop. If you pass Start, collect $200.":
                    if (player.getCurrentTile() > 25) {
                        player.addMoney(200);
                    }
                    player.setCurrentTile(25);
                    break;
                case "Make sure no-one leaks the street race location to the police. Pay each player $50.":
                    for (Player other : game.getPlayers()) {
                        if (other != player) {
                            player.pay(50);
                            other.addMoney(50);
                        }
                    }
                    break;
                case "Your betting on an F1 race was correct. Collect $150.":
                    player.addMoney(150);
                    break;
            }
        } else {
            card = community.get(new Random().nextInt(community.size()));
            moves.add(new Move(tiles.get(player.getCurrentTile()), card));
            switch (card) {
                case "Advance to Start (Collect $200)":
                    player.setCurrentTile(0);
                    player.addMoney(200);
                    break;
                case "Bank error in your favor. Collect $200":
                    player.addMoney(200);
                    break;
                case "You have been caught drunk driving. Pay $50":
                case "Speeding ticket. Pay $50":
                    player.pay(50);
                    break;
                case "From selling some car parts you get $50":
                    player.addMoney(50);
                    break;
                case "Get out of jail free":
                    player.addJailCard();
                    break;
                case "Go to jail. Go directly to jail, do not pass Start, do not collect $200":
                    player.goToJail();
                    break;
                case "You have won a cleanest car contest. Receive $100":
                case "You have won a street race. You win $100":
                    player.addMoney(100);
                    break;
                case "You have cleaned the neighbors' car. Collect $20":
                    player.addMoney(20);
                    break;
                case "You parked illegally. Pay $10":
                    player.pay(10);
                    break;
                case "You have lost a 10mm socket. Pay $15":
                    player.pay(15);
                    break;
                case "You rented a circuit. Pay $100":
                case "You bought a spoiler for $100":
                    player.pay(100);
                    break;
                case "Pay $25 for fixing minor scratches":
                    player.pay(25);
                    break;
                case "You installed illegal car modifications. Pay $40 fee per visual mod and $115 per performance mod":
                    List<Street> streetProps = player.getProperties().stream().filter(p -> p instanceof Street).map(p -> (Street) p).collect(Collectors.toList());
                    for (Street property : streetProps) {
                        player.pay(property.getHouseCount() * 40);
                        player.pay(property.getHotelCount() * 115);
                    }
                    break;
            }
        }
    }

    public int[] getRoll() {
        return roll;
    }

    public String getPlayer() {
        return player.getName();
    }

    public String getType() {
        return type;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public Move getLastMove() {
      return moves.get(moves.size() - 1);
    }
}
