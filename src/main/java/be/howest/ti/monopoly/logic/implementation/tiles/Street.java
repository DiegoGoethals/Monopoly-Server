package be.howest.ti.monopoly.logic.implementation.tiles;

import java.util.List;
import java.util.stream.Collectors;

public class Street extends Property {

    private int houseCount;
    private int hotelCount;
    private int rentWithOneHouse;
    private int rentWithTwoHouses;
    private int rentWithThreeHouses;
    private int rentWithFourHouses;
    private int rentWithHotel;
    private int housePrice;

    public Street(int position, String property, String type, int cost, int rent, int groupSize, String color, int rentWithOneHouse,
                  int rentWithTwoHouses, int rentWithThreeHouses, int rentWithFourHouses, int rentWithHotel, int housePrice) {
        super(position, property, type, cost, rent, groupSize, color);
        houseCount = 0;
        hotelCount = 0;
        this.rentWithOneHouse = rentWithOneHouse;
        this.rentWithTwoHouses = rentWithTwoHouses;
        this.rentWithThreeHouses = rentWithThreeHouses;
        this.rentWithFourHouses = rentWithFourHouses;
        this.rentWithHotel = rentWithHotel;
        this.housePrice = housePrice;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public int getRentWithOneHouse() {
        return rentWithOneHouse;
    }

    public int getRentWithTwoHouses() {
        return rentWithTwoHouses;
    }

    public int getRentWithThreeHouses() {
        return rentWithThreeHouses;
    }

    public int getRentWithFourHouses() {
        return rentWithFourHouses;
    }

    public int getRentWithHotel() {
        return rentWithHotel;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public boolean isUpgradeable() {
        List<Street> sameColor = getOwner().getProperties().stream()
                .filter(p -> p instanceof Street)
                .map(p -> (Street) p)
                .filter(s -> s.getColor().equals(getColor()))
                .collect(Collectors.toList());
        int lowestHouseCount = sameColor.stream()
                .mapToInt(Street::getHouseCount)
                .min()
                .orElse(0);
        return sameColor.size() == getGroupSize() && lowestHouseCount == houseCount &&
          houseCount <= 4 && hotelCount == 0;
    }

    public void buyHouse() {
        houseCount++;
    }

}
