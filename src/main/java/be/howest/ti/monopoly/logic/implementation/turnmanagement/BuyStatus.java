package be.howest.ti.monopoly.logic.implementation.turnmanagement;

public class BuyStatus {

    private String property;
    private String description;

    public BuyStatus(String property, String description) {
        this.property = property;
        this.description = description;
    }

    public String getProperty() {
        return property;
    }

    public String getDescription() {
        return description;
    }
}
