package de.dis2011.data;

public class House extends Estate {

    public static final String DB_COLUMN_FLOORS = "FLOORS";
    public static final String DB_COLUMN_PRICE = "PRICE";
    public static final String DB_COLUMN_GARDEN = "GARDEN";
    public static final String DB_COLUMN_PURCHASE_CONTRACT = "PURCHASE_CONTRACT";
    public static final String DB_COLUMN_PERSON = "PERSON";


    private int floors;
    private double price;
    private int garden; // 0 or 1
    private int purchaseContract;
    private int person;

    public House(Estate estate) {
        this.setId(estate.getId());
        this.setCity(estate.getCity());
        this.setPostalCode(estate.getPostalCode());
        this.setStreet(estate.getStreet());
        this.setStreetNumber(estate.getStreetNumber());
        this.setSquareArea(estate.getSquareArea());
        this.setEstateAgent(estate.getEstateAgent());
    }


    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGarden() {
        return garden;
    }

    public void setGarden(int garden) {
        this.garden = garden;
    }

    public int getPurchaseContract() {
        return purchaseContract;
    }

    public void setPurchaseContract(int purchaseContract) {
        this.purchaseContract = purchaseContract;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }
}
