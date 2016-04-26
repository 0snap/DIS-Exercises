package de.dis2011.data;

import java.sql.Date;

public class TenancyContract extends Contract {

    // tenancy fields
    public static final String DB_COLUMN_START_DATE = "START_DATE";
    public static final String DB_COLUMN_DURATION = "DURATION";
    public static final String DB_COLUMN_COST = "ADDITIONAL_COSTS";
    public static final String DB_COLUMN_RENTER = "RENTER";
    public static final String DB_COLUMN_OWNER = "OWNER";


    private Date startDate;
    private int duration;
    private double additionalCost;
    private int renter;
    private int owner;


    public TenancyContract() {
        //
    }
    public TenancyContract(Contract contract) {
        this.setDate(contract.getDate());
        this.setPlace(contract.getPlace());
        this.setId(contract.getId());
    }

    public int getRenter() {
        return renter;
    }

    public void setRenter(int renter) {
        this.renter = renter;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public double getAdditionalCost() {
        return additionalCost;
    }

    public void setAdditionalCost(double additionalCost) {
        this.additionalCost = additionalCost;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("TenancyContract no. %s, Owner: %s, Renter: %s", getId(), getOwner(), getRenter());
    }
}
