package com.data;

public class ContactBean {

	private String displayName;
	private String Number;
	
	
	
	public ContactBean(String displayName, String number) {
		super();
		this.displayName = displayName;
		Number = number;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		Number = number;
	}

	@Override
	public String toString() {
		return displayName + "\n"+ Number;
	}
	
	
}
