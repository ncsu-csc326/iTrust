package edu.ncsu.csc.itrust.cucumber;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust.controller.officeVisit.OfficeVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeData;
import edu.ncsu.csc.itrust.model.apptType.ApptTypeMySQLConverter;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisitMySQL;
import cucumber.api.java.en.Then;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;


public class OfficeVisitStepDefs {
private PatientDataShared patientData;
private OfficeVisitMySQL ovData;
private ApptTypeData atBean;
private HospitalData hospBean;
private DataSource ds;
private OfficeVisit sharedVisit;
private OfficeVisitController ovc;

	public OfficeVisitStepDefs(PatientDataShared currentPatient, OfficeVisit sharedOV){
		this.ds =ConverterDAO.getDataSource();
		this.patientData = currentPatient;
		this.ovData = new OfficeVisitMySQL(ds);
		this.atBean = new ApptTypeMySQLConverter(ds);
		this.hospBean = new HospitalMySQLConverter(ds);
		this.sharedVisit = sharedOV;
		this.ovc = new OfficeVisitController(ds);
	}
	
	@Given("^I have already selected (.+)")
	public void already_selected_patient(String patient){

		Long id =Long.parseLong(patient);
		if((id <=0L) ||(id > 8999999999L) ){
			throw new IllegalStateException("Not a valid patient MID");
		}

		patientData.patientID = id;
		
	}
	@Given("^(.+) already has a visit with date: (.+), location: (.+), appointment type: (.+), notes: (.+), and select send a bill (.+)$")
	public void previous_valid_visit(String patient, String date, String location, String apptType, String notes, String bill){
		OfficeVisit input = new OfficeVisit();
		Long id =Long.parseLong(patient);
		if((id <=0L) ||(id > 8999999999L) ){
			throw new IllegalStateException("Not a valid patient MID");
		}
		input.setPatientMID(id);
		

		patientData.patientID = id;
		
		boolean sendBill;
		if(bill.toLowerCase().trim().startsWith("f") && ((bill.toLowerCase().trim().endsWith("alse")) || (bill.toLowerCase().trim().length() ==1))) sendBill = false;
		else{
			if((bill.toLowerCase().trim().startsWith("t"))&& ((bill.toLowerCase().trim().endsWith("rue")) || (bill.toLowerCase().trim().length() ==1))) sendBill = true;
			else{
				Assert.fail("Invalid value for sendBill boolean");
				throw new InvalidParameterException("Invalid value for sendBill boolean");
				
			}
		}
			try {
				input.setApptTypeID(atBean.getApptTypeIDs(apptType).keySet().iterator().next());
		
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime ldtVisitDate= LocalDateTime.parse(date, formatter);
				input.setDate(ldtVisitDate);
				input.setLocationID(getFirstIDforHospitalName(location));
				input.setPatientMID(id);
				input.setNotes(notes);
				input.setSendBill(sendBill);
				ovData.add(input);
				sharedVisit.setApptTypeID(input.getApptTypeID());
				sharedVisit.setDate(input.getDate());
				sharedVisit.setLocationID(input.getLocationID());
				sharedVisit.setNotes(input.getNotes());
				sharedVisit.setPatientMID(input.getPatientMID());
				sharedVisit.setSendBill(input.getSendBill());
			} catch (DBException e) {
				Assert.fail("Office Visit Setup incorrect");
			}


	}
	@When("^I enter date: (.+), location: (.+), appointment type: (.+), and no other information$")
	public void enter_basic_ov_values_for_selected_patient(String visitDate, String visitLocation, String appointmentType){
		OfficeVisit ov = new OfficeVisit();
		
		try {
			ov.setApptTypeID(atBean.getApptTypeIDs(appointmentType).keySet().iterator().next());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime ldtVisitDate= LocalDateTime.parse(visitDate, formatter);
			ov.setDate(ldtVisitDate);
			ov.setLocationID(getFirstIDforHospitalName(visitLocation));
			ov.setPatientMID(patientData.patientID);
			ovData.add(ov);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		
	}
	
	
	@When("^I update the office visit information to date: (.+), location: (.+), appointment type: (.+), notes: (.+), and send a bill: (.+)$")
	public void update_ov_values_for_selected_patient(String visitDate, String visitLocation, String appointmentType, String notes, String bill) throws DBException{
		OfficeVisit ov = new OfficeVisit();
		List<OfficeVisit> all = ovData.getVisitsForPatient(patientData.patientID);
		Long visitID = null;
		boolean sendBill;
		if(bill.toLowerCase().trim().startsWith("f") && ((bill.toLowerCase().trim().endsWith("alse")) || (bill.toLowerCase().trim().length() ==1))) sendBill = false;
		else{
			if((bill.toLowerCase().trim().startsWith("t"))&& ((bill.toLowerCase().trim().endsWith("rue")) || (bill.toLowerCase().trim().length() ==1))) sendBill = true;
			else{
				Assert.fail("Invalid value for sendBill boolean");
				throw new InvalidParameterException("Invalid value for sendBill boolean");
				
			}
		}
		ov.setApptTypeID(atBean.getApptTypeIDs(appointmentType).keySet().iterator().next());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime ldtVisitDate= LocalDateTime.parse(visitDate, formatter);
		ov.setDate(ldtVisitDate);
		ov.setLocationID(getFirstIDforHospitalName(visitLocation));
		ov.setPatientMID(patientData.patientID);
		ov.setNotes(notes);
		ov.setSendBill(sendBill);
		for(int o = 0; o<all.size(); o++){
			OfficeVisit temp = all.get(o);
			boolean dateBool = (temp.getDate().equals(sharedVisit.getDate()));
			boolean locBool = (temp.getLocationID().equals(sharedVisit.getLocationID()));
			boolean midBool = (temp.getPatientMID().equals(sharedVisit.getPatientMID()));
			boolean notesBool = (temp.getNotes().equals(sharedVisit.getNotes()));
			if(!notesBool){
				if((temp.getNotes() ==null) && (sharedVisit.getNotes() == null)){
					notesBool = true;
				}
			}
			boolean billBool = (temp.getSendBill() == sharedVisit.getSendBill());
			
			if(dateBool && locBool && midBool && notesBool && billBool){
				visitID = all.get(o).getVisitID();
				ov.setVisitID(visitID);
			}
			
		}
		if(visitID == null){
			Assert.fail("Invalid Visit ID for update");
		}
		else{
			ovData.update(ov);
		}

	
		
	}

	@Then("^the patient's office visit for (.+), (.+), and (.+) is successfully documented$")
	public void required_info_office_visit_successfully_created(String date, String loc, String apptType){
		boolean retval = false;
		try{
			List<OfficeVisit> visits = ovData.getVisitsForPatient(patientData.patientID);
			for(int i=0; i<visits.size(); i++){
				OfficeVisit o = visits.get(i);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formatDate = o.getDate().format(formatter);
				boolean bDate = (formatDate.equals(date));
				boolean bLoc = (hospBean.getHospitalName(o.getLocationID()).equals(loc)) ;
				boolean bApptType = (atBean.getApptTypeName(o.getApptTypeID()).equals(apptType));
				boolean bPatient = (o.getPatientMID().equals(patientData.patientID));
				if(bDate && bLoc && bApptType && bPatient){
					retval = true;
					break;
				}
			}
			
			Assert.assertTrue("office visit does not exist",retval);
		}
		catch(DBException dbe){
			Assert.fail();
		}
		
	}

	@When("^I enter date: (.+), location: (.+), appointment type: (.+), notes: (.+), and select send a bill (.+)$")
	public void enter_all_ov_values_for_selected_patient(String visitDate, String visitLocation, String appointmentType, String notes, String bill){
		OfficeVisit ov = new OfficeVisit();
		boolean sendBill;
		if(bill.toLowerCase().trim().startsWith("f") && ((bill.toLowerCase().trim().endsWith("alse")) || (bill.toLowerCase().trim().length() ==1))) sendBill = false;
		else{
			if((bill.toLowerCase().trim().startsWith("t"))&& ((bill.toLowerCase().trim().endsWith("rue")) || (bill.toLowerCase().trim().length() ==1))) sendBill = true;
			else{
				Assert.fail("Invalid value for sendBill boolean");
				throw new InvalidParameterException("Invalid value for sendBill boolean");
				
			}
		}
		
		try {
			ov.setApptTypeID(atBean.getApptTypeIDs(appointmentType).keySet().iterator().next());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime ldtVisitDate= LocalDateTime.parse(visitDate, formatter);
			ov.setDate(ldtVisitDate);
			ov.setLocationID(getFirstIDforHospitalName(visitLocation));
			ov.setPatientMID(patientData.patientID);
			ov.setNotes(notes);
			ov.setSendBill(sendBill);
			ovData.add(ov);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	private String getFirstIDforHospitalName(String visitLocation) throws DBException {
		String LocID = "";
		List<Hospital> hList = hospBean.getAll();
		for(int i=0; i < hList.size(); i++){
			if(hList.get(i).getHospitalName().contains(visitLocation)){
				LocID = hList.get(i).getHospitalID();
				break;
			}
		}
		return LocID;
	}

	@Then("^the patient's entire office visit for (.+), (.+), (.+), (.+), and send bill (.+) is successfully documented$")
	public void all_info_office_visit_successfully_created(String date, String loc, String apptType, String notes, String bill){
		boolean retval = false;
		boolean sendBill;
		if(bill.toLowerCase().trim().startsWith("f") && ((bill.toLowerCase().trim().endsWith("alse")) || (bill.toLowerCase().trim().length() ==1))) sendBill = false;
		else{
			if((bill.toLowerCase().trim().startsWith("t"))&& ((bill.toLowerCase().trim().endsWith("rue")) || (bill.toLowerCase().trim().length() ==1))) sendBill = true;
			else{
				Assert.fail("Invalid value for sendBill boolean");
				throw new InvalidParameterException("Invalid value for sendBill boolean");
				
			}
		}
	
		try{
			List<OfficeVisit> visits = ovc.getOfficeVisitsForPatient(Long.toString(patientData.patientID));
			for(int i=0; i<visits.size(); i++){
				OfficeVisit o = visits.get(i);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formatDate = o.getDate().format(formatter);
				boolean bDate = (formatDate.equals(date));
				boolean bLoc = (hospBean.getHospitalName(o.getLocationID()).equals(loc)) ;
				boolean bApptType = (atBean.getApptTypeName(o.getApptTypeID()).equals(apptType));
				boolean bPatient = (o.getPatientMID().equals(patientData.patientID));
				boolean bNotes = (o.getNotes().equals(notes));
				boolean bSendBill = (o.getSendBill() == sendBill);
				if(bDate && bLoc && bApptType && bPatient && bNotes && bSendBill){
					retval = true;
					break;
				}
			}
			
			Assert.assertTrue("office visit does not exist",retval);
		}
		catch(DBException dbe){
			Assert.fail();
		}
		
	}


	@Then("^the office visit information is - date: (.+), location: (.+), appointment type: (.+), notes: (.+), and send a bill: (.+)$")
	public void required_info_office_visit_successfully_created(String date, String loc, String apptType, String notes, String bill){
		boolean retval = false;
		boolean sendBill;
		if(bill.toLowerCase().trim().startsWith("f") && ((bill.toLowerCase().trim().endsWith("alse")) || (bill.toLowerCase().trim().length() ==1))) sendBill = false;
		else{
			if((bill.toLowerCase().trim().startsWith("t"))&& ((bill.toLowerCase().trim().endsWith("rue")) || (bill.toLowerCase().trim().length() ==1))) sendBill = true;
			else{
				Assert.fail("Invalid value for sendBill boolean");
				throw new InvalidParameterException("Invalid value for sendBill boolean");
				
			}
		}
	
		try{
			List<OfficeVisit> visits = ovData.getVisitsForPatient(patientData.patientID);
			for(int i=0; i<visits.size(); i++){
				OfficeVisit o = visits.get(i);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				String formatDate = o.getDate().format(formatter);
				boolean bDate = (formatDate.equals(date));
				boolean bLoc = (hospBean.getHospitalName(o.getLocationID()).equals(loc)) ;
				boolean bApptType = (atBean.getApptTypeName(o.getApptTypeID()).equals(apptType));
				boolean bPatient = (o.getPatientMID().equals(patientData.patientID));
				boolean bNotes = (o.getNotes().equals(notes));
				boolean bSendBill = (o.getSendBill() == sendBill);
				if(bDate && bLoc && bApptType && bPatient && bNotes && bSendBill){
					retval = true;
					break;
				}
			}
			
			Assert.assertTrue("office visit does not exist",retval);
		}
		catch(DBException dbe){
			Assert.fail();
		}
	}

}
