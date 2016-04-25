package de.dis2011.data;


public class Apartment extends Estate {

    public static final String DB_COLUMN_FLOOR = "FLOOR";
    public static final String DB_COLUMN_RENT = "RENT";
    public static final String DB_COLUMN_ROOMS = "ROOMS";
    public static final String DB_COLUMN_BALCONY = "BALCONY";
    public static final String DB_COLUMN_BUILT_IN_KITCHEN = "BUILT_IN_KITCHEN";
    public static final String DB_COLUMN_TENANCY_CONTRACT = "TENANCY_CONTRACT";
    public static final String DB_COLUMN_PERSON = "PERSON";

    private int floor;
    private double rent;
    private int rooms;
    private int balcony; // 0 or 1
    private int builtInKitchen; // 0 or 1
    private int tenancyContract;
    private int person;


    public Apartment(Estate estate) {
        this.setId(estate.getId());
        this.setCity(estate.getCity());
        this.setPostalCode(estate.getPostalCode());
        this.setStreet(estate.getStreet());
        this.setStreetNumber(estate.getStreetNumber());
        this.setSquareArea(estate.getSquareArea());
        this.setEstateAgent(estate.getEstateAgent());
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBalcony() {
        return balcony;
    }

    public void setBalcony(int balcony) {
        this.balcony = balcony;
    }

    public int getBuiltInKitchen() {
        return builtInKitchen;
    }

    public void setBuiltInKitchen(int builtInKitchen) {
        this.builtInKitchen = builtInKitchen;
    }

    public int getTenancyContract() {
        return tenancyContract;
    }

    public void setTenancyContract(int tenancyContract) {
        this.tenancyContract = tenancyContract;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }
}
