package edu.ncsu.csc.itrust.controller.hospital;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
import edu.ncsu.csc.itrust.model.hospital.HospitalData;
import edu.ncsu.csc.itrust.model.hospital.HospitalMySQLConverter;

@ManagedBean(name="hospital_controller")
public class HospitalController {
	private static HospitalData hospitalData;
	public HospitalController() throws DBException{
		if(hospitalData == null){
			HospitalController.hospitalData = new HospitalMySQLConverter();
			
		}
		
	}
	
	//Test Constructor
	public HospitalController(DataSource ds) throws DBException{
		if(hospitalData == null){
			HospitalController.hospitalData = new HospitalMySQLConverter(ds);
			
		}
		
	}

	public List<Hospital> getHospitalList() throws DBException{
		return hospitalData.getAll();
	}
	
	public String HospitalNameForID(String hospitalID){
		try {
			return hospitalData.getHospitalName(hospitalID);
		} catch (Exception e) {
			FacesMessage throwMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Location Information", "Invalid Location Information");
	        FacesContext.getCurrentInstance().addMessage(null,throwMsg);
	        return "";

		}
	}

}
