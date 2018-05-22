package edu.ncsu.csc.itrust.controller.officeVisit;

import java.time.LocalDateTime;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

@ManagedBean(name = "office_visit_form")
@ViewScoped
public class OfficeVisitForm {
	private OfficeVisitController controller;
	private OfficeVisit ov;
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

	public Long getVisitID() {
		return visitID;
	}

	public void setVisitID(Long visitID) {
		this.visitID = visitID;
	}

	public Long getPatientMID() {
		return patientMID;
	}

	public void setPatientMID(Long patientMID) {
		this.patientMID = patientMID;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public Long getApptTypeID() {
		return apptTypeID;
	}

	public void setApptTypeID(Long apptTypeID) {
		this.apptTypeID = apptTypeID;
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
	 * @param patientSmokingStatus
	 *            the patientSmokingStatus to set
	 */
	public void setPatientSmokingStatus(Integer patientSmokingStatus) {
		this.patientSmokingStatus = patientSmokingStatus;
	}

	/**
	 * Default constructor for OfficeVisitForm.
	 */
	public OfficeVisitForm() {
		this(null);
	}

	/**
	 * Constructor for OfficeVisitForm for testing purpses.
	 */
	public OfficeVisitForm(OfficeVisitController ovc) {
		try {
			controller = (ovc == null) ? new OfficeVisitController() : ovc;
			ov = controller.getSelectedVisit();
			if (ov == null) {
				ov = new OfficeVisit();
			}
			try {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", ov.getVisitID());
			} catch (NullPointerException e) {
				// Do nothing
			}
			visitID = ov.getVisitID();
			patientMID = ov.getPatientMID();
			if (patientMID == null) {
				patientMID = Long.parseLong(
				(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pid"));
			}
			date = ov.getDate();
			locationID = ov.getLocationID();
			apptTypeID = ov.getApptTypeID();
			sendBill = ov.getSendBill();
			notes = ov.getNotes();
			
			height = ov.getHeight();
			length = ov.getLength();
			weight = ov.getWeight();
			headCircumference = ov.getHeadCircumference();
			bloodPressure = ov.getBloodPressure();
			hdl = ov.getHDL();
			triglyceride = ov.getTriglyceride();
			ldl = ov.getLDL();
			householdSmokingStatus = ov.getHouseholdSmokingStatus();
			patientSmokingStatus = ov.getPatientSmokingStatus();

		} catch (Exception e) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Office Visit Controller Error",
					"Office Visit Controller Error");
			FacesContext.getCurrentInstance().addMessage(null, throwMsg);
		}
	}
	
	/**
	 * Called when user clicks on the submit button in officeVisitInfo.xhtml. Takes data from form
	 * and sends to OfficeVisitMySQLLoader.java for storage and validation
	 */
	public void submit() {
		ov.setApptTypeID(apptTypeID);
		ov.setDate(date);
		ov.setLocationID(locationID);
		ov.setNotes(notes);
		ov.setSendBill(sendBill);
		ov.setPatientMID(patientMID);
		
		if (isOfficeVisitCreated()) {
			controller.edit(ov);
			controller.logTransaction(TransactionType.OFFICE_VISIT_EDIT, ov.getVisitID().toString());
		} else {
			long pid = -1;
			
			FacesContext ctx = FacesContext.getCurrentInstance();

			String patientID = "";
			
			if (ctx.getExternalContext().getRequest() instanceof HttpServletRequest) {
				HttpServletRequest req = (HttpServletRequest) ctx.getExternalContext().getRequest();
				HttpSession httpSession = req.getSession(false);
				patientID = (String) httpSession.getAttribute("pid");
			}
			if (ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) {
				pid = Long.parseLong(patientID);
			}
			
			ov.setPatientMID(pid);
			ov.setVisitID(null);
			long generatedVisitId = controller.addReturnGeneratedId(ov);
			setVisitID(generatedVisitId);
			ov.setVisitID(generatedVisitId);
			controller.logTransaction(TransactionType.OFFICE_VISIT_CREATE, ov.getVisitID().toString());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("officeVisitId", generatedVisitId);
		}
	}
	
	/**
	 * Called when user updates health metrics on officeVisitInfo.xhtml.
	 */
	public void submitHealthMetrics() {
        boolean isNew = ov.getHouseholdSmokingStatus() == null || ov.getHouseholdSmokingStatus() == 0;
		// Some error checking here?
		ov.setHeight(height);
		ov.setLength(length);
		ov.setWeight(weight);
		ov.setHeadCircumference(headCircumference);
		ov.setBloodPressure(bloodPressure);
		ov.setHDL(hdl);
		ov.setTriglyceride(triglyceride);
		ov.setLDL(ldl);
		ov.setHouseholdSmokingStatus(householdSmokingStatus);
		ov.setPatientSmokingStatus(patientSmokingStatus);
		controller.edit(ov);
		if (isNew){
		    controller.logTransaction(TransactionType.CREATE_BASIC_HEALTH_METRICS, "Age: " + controller.calculatePatientAge(patientMID, date).toString());
		} else {
		    controller.logTransaction(TransactionType.EDIT_BASIC_HEALTH_METRICS, "Age: " + controller.calculatePatientAge(patientMID, date));
		}
	}
	
	/**
	 * @return true if the Patient is a baby 
	 */
	public boolean isPatientABaby() {
		return controller.isPatientABaby(getPatientMID(), getDate());
	}
	
	/**
	 * @return true if the Patient is a child 
	 */
	public boolean isPatientAChild() {
		return controller.isPatientAChild(getPatientMID(), getDate());
	}
	
	/**
	 * @return true if the Patient is an adult 
	 */
	public boolean isPatientAnAdult() {
		return controller.isPatientAnAdult(getPatientMID(), getDate());
	}
	
	public boolean isOfficeVisitCreated() {
		return (visitID != null) && (visitID > 0);
	}
}
