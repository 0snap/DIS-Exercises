package de.dis2011.data;


/**
 * Contract-Pojo
 */
public class Contract {

    //Basic Contract
    public static final String DB_COLUMN_ID = "ID";
    public static final String DB_COLUMN_CONTRACT_NO = "CONTRACT_NO";
    public static final String DB_COLUMN_DATE = "DATE";
    public static final String DB_COLUMN_PLACE = "PLACE";
    // switch for tenancy/purchase
    public static final String DB_COLUMN_IS_TENANCY = "IS_TENANCY";

    // tenancy fields
    public static final String DB_COLUMN_START_DATE = "START_DATE";
    public static final String DB_COLUMN_DURATION = "DURATION";
    public static final String DB_COLUMN_COST = "COST";

    // purchase fields
    public static final String DB_COLUMN_INSTALLMENTS = "INSTALLMENTS";
    public static final String DB_COLUMN_INTEREST = "INTEREST";

    private int id = -1;
    private String contract_no;
    private String date;
    private String place;
    private boolean is_tenancy;
    private String start_date;
    private int duration;
    private int cost;
    private String installments;
    private int interest;


    public void set_id(int value){
        this.id = value
    }

    public int get_id(){
        return this.id
    }

    public voiding set_contract_no(String value){
        this.contract_no=value
    }
}
    public String get_contract_no(){
        return this.contract_no
    }

    public voiding set_date(String value){
        this.date=value
    }
    public String get_date(){
        return this.date
    }

    public voiding set_place(String value){
        this.place=value
    }
    public String get_place(){
        return this.place
    }

    public voidlean set_is_tenancy(boolean value){
        this.is_tenancy=value
    }
    public boolean get_is_tenancy(){
        return this.is_tenancy
    }

    public voiding set_start_date(String value){
        this.start_date=value
    }
    public String get_start_date(){
        return this.start_date
    }

    public void set_duration(int value){
        this.duration=value
    }

    public int get_duration(){
        return this.duration
    }

    public void set_cost(int value){
        this.cost=value
    }

    public int get_cost(){
        return this.cost
    }

    public voiding set_installments(String value){
        this.installments=value
    }

    public String get_installments(){
        return this.installments
    }

    public void set_interest(int value){
        this.interest=value
    }

    public int get_interest(){
        return this.interest
    }

