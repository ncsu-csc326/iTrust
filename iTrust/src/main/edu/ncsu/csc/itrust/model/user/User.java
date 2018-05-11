package edu.ncsu.csc.itrust.model.user;

import java.io.Serializable;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.enums.Role;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4883990864493568300L;
	private long MID;
	private String lastName;
	private String firstName;
	private Role role;
	public User(){
		
	}
	public long getMID() {
		return MID;
	}
	public void setMID(long mid) throws ITrustException {
		if((mid >= 10000000000L) || (mid<=0L)){
			throw new ITrustException("Invalid MID");
		}
		else{
			MID = mid;
		}
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
