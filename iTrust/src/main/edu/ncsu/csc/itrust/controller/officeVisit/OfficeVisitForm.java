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

@ManagedBean(name="office_visit_form")
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

	public OfficeVisitForm(){
		try {
			controller = new OfficeVisitController();
			ov = controller.getSelectedVisit();
			if(ov == null){
				ov = new OfficeVisit();
			}
			visitID = ov.getVisitID();
			patientMID = ov.getPatientMID();
			date = ov.getDate();
			locationID = ov.getLocationID();
			apptTypeID = ov.getApptTypeID();
			sendBill = ov.getSendBill();
			notes = ov.getNotes();
			
		} catch (Exception e) {
	      	FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Office Visit Controller Error", "Office Visit Controller Error");
	        FacesContext.getCurrentInstance().addMessage(null,throwMsg);


		}
		
		
	}
	
	public void submit(){
		ov.setApptTypeID(apptTypeID);
		ov.setDate(date);
		ov.setLocationID(locationID);
		ov.setNotes(notes);
		ov.setSendBill(sendBill);
		ov.setPatientMID(patientMID);
		if((visitID != null) && (visitID>0)){
			ov.setVisitID(visitID);
			controller.edit(ov);

		}
		else{
			long pid = -1;
			FacesContext ctx = FacesContext.getCurrentInstance();

			
			String patientID = "";
			if(ctx.getExternalContext().getRequest() instanceof HttpServletRequest){
				HttpServletRequest req = (HttpServletRequest)ctx.getExternalContext().getRequest();
				HttpSession httpSession = req.getSession(false);
				patientID = (String) httpSession.getAttribute("pid");
			}
			if(ValidationFormat.NPMID.getRegex().matcher(patientID).matches()){
				pid = Long.parseLong(patientID);
			}
			ov.setPatientMID(pid);
			ov.setVisitID(null);

			controller.add(ov);

		}
	}

}
