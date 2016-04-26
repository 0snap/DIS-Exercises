package de.dis2011.data;

public class PurchaseContract extends Contract {

    // purchase fields
    public static final String DB_COLUMN_INSTALLMENTS = "NUMBER_OF_INSTALLMENTS";
    public static final String DB_COLUMN_INTEREST = "INTEREST_RATE";
    public static final String DB_COLUMN_SELLER = "SELLER";
    public static final String DB_COLUMN_BUYER = "BUYER";

    private int installments;
    private int interest;
    private int seller;
    private int buyer;


    public PurchaseContract() {
        //
    }
    public PurchaseContract(Contract contract) {
        this.setDate(contract.getDate());
        this.setPlace(contract.getPlace());
        this.setId(contract.getId());
    }

    public int getSeller() {
        return seller;
    }

    public void setSeller(int seller) {
        this.seller = seller;
    }

    public int getBuyer() {
        return buyer;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

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

    @Override
    public String toString() {
        return String.format("PurchaseContract no. %s, Buyer: %s, Seller: %s", getId(), getBuyer(), getSeller());
    }
}
