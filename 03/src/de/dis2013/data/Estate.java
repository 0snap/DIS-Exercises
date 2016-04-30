package de.dis2013.data;

import de.dis2013.util.Helper;

/**
 * Immobilien-Bean
 */
public abstract class Estate {
	private int id = -1;
	private String city;
	private int postalCode;
	private String street;
	private String streetNumber;
	private int squareArea;
	private EstateAgent estateAgent;
	static int currentId = 0;
	
	public Estate() {
		this.id = currentId++;
	}
	
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
	public int getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}
	public int getSquareArea() {
		return squareArea;
	}
	public void setSquareArea(int squareArea) {
		this.squareArea = squareArea;
	}
	
	public void setEstateAgent(EstateAgent estateAgent) {
		this.estateAgent = estateAgent;
	}

	public EstateAgent getEstateAgent() {
		return estateAgent;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
		result = prime * result + ((getStreet() == null) ? 0 : getStreet().hashCode());
		result = prime * result + ((getStreetNumber() == null) ? 0 : getStreetNumber().hashCode());
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof Estate))
			return false;
	
		Estate other = (Estate)obj;
	
		if(other.getId() != getId() ||
				other.getPostalCode() != getPostalCode() ||
				other.getSquareArea() != getSquareArea() ||
				!Helper.compareObjects(this.getCity(), other.getCity()) ||
				!Helper.compareObjects(this.getStreet(), other.getStreet()) ||
				!Helper.compareObjects(this.getStreetNumber(), other.getStreetNumber()))
		{
			return false;
		}
		
		return true;
	}
}
