package de.dis2011.data;


/**
 * Estate-Pojo
 */
public class Estate {

    public static final String DB_COLUMN_ID = "ID";
    public static final String DB_COLUMN_CITY = "CITY";
    public static final String DB_COLUMN_POSTAL_CODE = "POSTAL_CODE";
	public static final String DB_COLUMN_STREET = "STREET";
	public static final String DB_COLUMN_STREET_NUMBER = "STREET_NUMBER";
	public static final String DB_COLUMN_SQUARE_AREA = "SQUARE_AREA";
	public static final String DB_COLUMN_ESTATE_AGENT = "ESTATE_AGENT";

	private int id = -1;
	private String city;
	private String postalCode;
	private String street;
	private int streetNumber;
	private String squareArea;
	private int estateAgent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getSquareArea() {
		return squareArea;
	}

	public void setSquareArea(String squareArea) {
		this.squareArea = squareArea;
	}

	public int getEstateAgent() {
		return estateAgent;
	}

	public void setEstateAgent(int estateAgent) {
		this.estateAgent = estateAgent;
	}
}
