package de.dis2013.data;

import de.dis2013.util.Helper;

public class PurchaseContract extends Contract {
	private int numberOfInstallments;
	private int interestRate;
	private House house;
	
	public PurchaseContract() {
		super();
	}
	
	public int getNumberOfInstallments() {
		return numberOfInstallments;
	}
	public void setNumberOfInstallments(int numberOfInstallments) {
		this.numberOfInstallments = numberOfInstallments;
	}
	public int getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(int interestRate) {
		this.interestRate = interestRate;
	}
	
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		
		result = prime * result + getNumberOfInstallments();
		result = prime * result + getInterestRate();
		
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || !(obj instanceof PurchaseContract))
			return false;
	
		PurchaseContract other = (PurchaseContract)obj;
	
		if(other.getContractNumber() != getContractNumber() ||
				!Helper.compareObjects(this.getDate(), other.getDate()) ||
				!Helper.compareObjects(this.getPlace(), other.getPlace()) ||
				other.getNumberOfInstallments() != getNumberOfInstallments() ||
				other.getInterestRate() != getInterestRate())
		{
			return false;
		}
		
		return true;
	}
}
