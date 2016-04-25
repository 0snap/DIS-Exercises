package de.dis2011.data;

public class PurchaseContract extends Contract {

    // purchase fields
    public static final String DB_COLUMN_INSTALLMENTS = "NUMBER_OF_INSTALLMENTS";
    public static final String DB_COLUMN_INTEREST = "INTEREST_RATE";

    private int installments;
    private int interest;

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }
}
