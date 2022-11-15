package be.howest.ti.monopoly.logic.implementation.tiles;

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

    @Override
    public int getHouseCount() {
        return houseCount;
    }

    @Override
    public int getHotelCount() {
        return hotelCount;
    }

    @Override
    public int getRentWithOneHouse() {
        return rentWithOneHouse;
    }

    @Override
    public int getRentWithTwoHouses() {
        return rentWithTwoHouses;
    }

    @Override
    public int getRentWithThreeHouses() {
        return rentWithThreeHouses;
    }

    @Override
    public int getRentWithFourHouses() {
        return rentWithFourHouses;
    }

    @Override
    public int getRentWithHotel() {
        return rentWithHotel;
    }

    @Override
    public int getHousePrice() {
        return housePrice;
    }

}
