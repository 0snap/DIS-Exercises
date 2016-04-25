package de.dis2011.data;

import java.util.Date;

public class TenancyContract extends Contract {

    // tenancy fields
    public static final String DB_COLUMN_START_DATE = "START_DATE";
    public static final String DB_COLUMN_DURATION = "DURATION";
    public static final String DB_COLUMN_COST = "ADDITIONAL_COSTS";


    private Date startDate;
    private int duration;
    private double additionalCost;

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
}
