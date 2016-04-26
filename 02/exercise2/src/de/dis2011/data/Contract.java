package de.dis2011.data;


import java.sql.Date;

/**
 * Contract-Pojo
 */
public class Contract {

    //Basic Contract
    public static final String DB_COLUMN_ID = "ID";
    public static final String DB_COLUMN_DATE = "DATE";
    public static final String DB_COLUMN_PLACE = "PLACE";

    private int id = -1;
    private Date date;
    private String place;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}