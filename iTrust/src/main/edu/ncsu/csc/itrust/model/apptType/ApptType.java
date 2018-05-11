package edu.ncsu.csc.itrust.model.apptType;

public class ApptType {
	private long ID;
	private String name;
	private int duration;
	private int price; 
	
	public ApptType() {
		this.name = null;
		this.duration = 0;
	}
	
	public ApptType(String name, int duration) {
		this.name = name;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
}
