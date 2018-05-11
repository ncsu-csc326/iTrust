/**
 * 
 */
package edu.ncsu.csc.itrust.model.officeVisit;

import java.time.LocalDateTime;

import javax.faces.bean.ManagedBean;

/**
 * @author seelder
 *
 */
@ManagedBean(name="office_visit")
public class OfficeVisit {

	
	private Long visitID;
	private Long patientMID;
	private LocalDateTime date;
	private String locationID;
	private Long apptTypeID;
	private String notes;
	private Boolean sendBill;
	
	public OfficeVisit(){
		sendBill = true;
	}
	
	
	/**
	 * @return the patientMID
	 */
	public long getPatientMID() {
		return patientMID;
	}

	/**
	 * @param patientID the patient MID to set
	 */
	public void setPatientMID(Long patientID) {
		this.patientMID = patientID;
	}

	/**
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * @param date the date of the office visit
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return the locationID
	 */
	public String getLocationID() {
		return locationID;
	}

	/**
	 * @param locationID the locationID to set
	 */
	public void setLocationID(String location) {
		this.locationID = location;
	}

	/**
	 * @return the apptTypeID
	 */
	public Long getApptTypeID() {
		return apptTypeID;
	}

	/**
	 * @param apptTypeID the apptTypeID to set
	 */
	public void setApptTypeID(Long apptType) {
		this.apptTypeID = apptType;
	}

	public Long getVisitID() {
		return visitID;
	}

	public void setVisitID(Long visitID) {
		this.visitID = visitID;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getSendBill() {
		return sendBill;
	}

	public void setSendBill(Boolean sendBill) {
		this.sendBill = sendBill;
	}
	



}
