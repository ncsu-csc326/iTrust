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
	
	private Float height;
	private Float length;
	private Float weight;
	private Float headCircumference;
	private String bloodPressure;
	private Integer hdl;
	private Integer triglyceride;
	private Integer ldl;
	private Integer householdSmokingStatus;
	private Integer patientSmokingStatus;
	
	/**
	 * Enum for household smoking status in basic health metrics
	 */
	public enum HouseholdSmokingStatus {
		NON_SMOKING_HOUSEHOLD(1, "Non-Smoking Household"),
		OUTDOOR_SMOKERS(2, "Outdoor Smokers"),
		INDOOR_SMOKERS(3, "Indoor Smokers"),
		UNSELECTED(0, "Unselected");
		
		private int id;
		private String description;
		HouseholdSmokingStatus(int id, String description) {
			this.id = id;
			this.description = description;
		}
		
		public static String getDesriptionById(int id) {
			for(HouseholdSmokingStatus status: values()) {
				if (status.id == id) {
					return status.description;
				}
			}
			return null;
		}
	}
	
	/**
	 * Enum for patient smoking status in basic health metrics
	 */
	public enum PatientSmokingStatus {
		CURRENT_EVERY_DAY_SMOKER(1, "Current Every Day Smoker"),
		CURRENT_SOME_DAY_SMOKER(2, "Current Some Day Smoker"),
		FORMER_SMOKER(3, "Former Smoker"),
		NEVER_SMOKER(4, "Never Smoker"),
		SMOKER_CURRENT_STATUS_UNKNOWN(5, "Smoker, current status unknown"),
		UNKNOWN_IF_EVER_SMOKED(9, "Unknown if ever smoked"),
		UNSELECTED(0, "Unselected");
		
		private int id;
		private String description;
		PatientSmokingStatus(int id, String description) {
			this.id = id;
			this.description = description;
		}
		
		public static String getDesriptionById(int id) {
			for(PatientSmokingStatus status: values()) {
				if (status.id == id) {
					return status.description;
				}
			}
			return null;
		}
	}
	
	/**
	 * Default constructor for OfficeVisit
	 */
	public OfficeVisit(){
		sendBill = true;
	}
	
	
	/**
	 * @return the patientMID
	 */
	public Long getPatientMID() {
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

	/**
	 * Returns the Height recorded at the office visit.
	 * 
	 * @return the Height recorded at the office visit.
	 */
	public Float getHeight() {
		return height;
	}

	/**
	 * Sets the height recorded at the office visit.
	 * 
	 * @param height
	 *            The height recorded at the office visit
	 */
	public void setHeight(Float height) {
		this.height = height;
	}

	/**
	 * Gets the length recorded at the office visit.
	 * 
	 * @param length
	 *            The height recorded at the office visit
	 */
	public Float getLength() {
		return length;
	}

	/**
	 * Sets the length recorded at the office visit.
	 * 
	 * @param length
	 *            The height recorded at the office visit
	 */
	public void setLength(Float length) {
		this.length = length;
	}

	/**
	 * Returns the weight recorded at the office visit.
	 * 
	 * @return the weight recorded at the office visit.
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * Returns the head circumference measured at the office visit.
	 * 
	 * @return the headCircumference
	 */
	public Float getHeadCircumference() {
		return headCircumference;
	}

	/**
	 * @param headCircumference
	 *            the headCircumference to set
	 */
	public void setHeadCircumference(Float headCircumference) {
		this.headCircumference = headCircumference;
	}

	/**
	 * Returns the blood pressure measured at the office visit.
	 * 
	 * @return the bloodPressure
	 */
	public String getBloodPressure() {
		return bloodPressure;
	}

	/**
	 * @param bloodPressure
	 *            the bloodPressure to set
	 */
	public void setBloodPressure(String bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	/**
	 * Returns the HDL cholesterol level measured at the office visit.
	 * 
	 * @return the hdl
	 */
	public Integer getHDL() {
		return hdl;
	}

	/**
	 * @param hdl
	 *            the hdl to set
	 */
	public void setHDL(Integer hdl) {
		this.hdl = hdl;
	}

	/**
	 * Returns the triglyceride cholesterol level measured at the office visit.
	 * 
	 * @return the triglyceride
	 */
	public Integer getTriglyceride() {
		return triglyceride;
	}

	/**
	 * @param triglyceride
	 *            the triglyceride to set
	 */
	public void setTriglyceride(Integer triglyceride) {
		this.triglyceride = triglyceride;
	}

	/**
	 * Returns the LDL cholesterol level measured at the office visit.
	 * 
	 * @return the ldl
	 */
	public Integer getLDL() {
		return ldl;
	}

	/**
	 * @param ldl
	 *            the ldl to set
	 */
	public void setLDL(Integer ldl) {
		this.ldl = ldl;
	}

	/**
	 * Returns the household smoking status indicated at the office visit.
	 * 
	 * @return the householdSmokingStatus
	 */
	public Integer getHouseholdSmokingStatus() {
		return householdSmokingStatus;
	}

	/**
	 * @param householdSmokingStatus
	 *            the householdSmokingStatus to set
	 */
	public void setHouseholdSmokingStatus(Integer householdSmokingStatus) {
		this.householdSmokingStatus = householdSmokingStatus;
	}

	/**
	 * Returns the patient smoking status indicated at the office visit.
	 * 
	 * @return the patientSmokingStatus
	 */
	public Integer getPatientSmokingStatus() {
		return patientSmokingStatus;
	}
	
	/**
	 * @return string representation of patient smoking status in the format of:
	 * 			"id - description"
	 */
	public String getPatientSmokingStatusDescription() {
		String description = PatientSmokingStatus.getDesriptionById(patientSmokingStatus);
		if (description == null) {
			return "";
		}
		return String.format("%d - %s", patientSmokingStatus, description);
	}

	/**
	 * @param patientSmokingStatus
	 *            the patientSmokingStatus to set
	 */
	public void setPatientSmokingStatus(Integer patientSmokingStatus) {
		this.patientSmokingStatus = patientSmokingStatus;
	}
	
	/**
	 * @return string representation of household smoking status in the format of:
	 * 			"id - description"
	 */
	public String getHouseholdSmokingStatusDescription() {
		String description = HouseholdSmokingStatus.getDesriptionById(householdSmokingStatus);
		if (description == null) {
			return "";
		}
		return String.format("%d - %s", householdSmokingStatus, description);
	}
	
	/**
	 * Calculates adult/child patient's BMI according to patient's height and weight.
	 * 
	 * @see http://extoxnet.orst.edu/faqs/dietcancer/web2/twohowto.html
	 * @return patient's BMI in 2 decimal places, "N/A" if patient's height or weight 
	 * 			is uninitialized or invalid
	 */
	public String getAdultBMI() {
		return getBMI(weight, height);
	}
	
	/**
	 * Calculates baby patient's BMI according to patient's height and length.
	 * 
	 * @see http://extoxnet.orst.edu/faqs/dietcancer/web2/twohowto.html
	 * @return patient's BMI in 2 decimal places, "N/A" if patient's height or weight 
	 * 			is uninitialized or invalid
	 */
	public String getBabyBMI() {
		return getBMI(weight, length);
	}
	
	/**
	 * Calculates BMI according to provided height and weight.
	 * 
	 * @see http://extoxnet.orst.edu/faqs/dietcancer/web2/twohowto.html
	 * @return BMI in 2 decimal places, "N/A" if given height or weight 
	 * 			is uninitialized or invalid
	 */
	public static String getBMI(Float weight, Float height) {
		if (weight == null || height == null || weight <= 0 || height <= 0) {
			return "N/A";
		}
		return String.format("%.2f", weight * 0.45 / Math.pow(height * 0.025, 2));
	}
}
