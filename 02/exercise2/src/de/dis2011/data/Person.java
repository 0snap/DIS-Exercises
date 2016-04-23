package de.dis2011.data;


/**
 * Person-Pojo
 * impudent copy-pasta of EstateAgent
 */
public class Person {

    public static final String DB_COLUMN_ID = "ID";
    public static final String DB_COLUMN_NAME = "NAME";
    public static final String DB_COLUMN_ADDRESS = "ADDRESS";

    private int id = -1;
    private String name;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
