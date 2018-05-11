package edu.ncsu.csc.itrust.controller.apptType;
import java.util.List;

import javax.faces.bean.ManagedBean;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.apptType.*;

@ManagedBean(name="appt_type_controller")
public class ApptTypeController {
	private static ApptTypeData apptTypeData;
	public ApptTypeController() throws DBException{
		if(apptTypeData == null){
			ApptTypeController.apptTypeData = new ApptTypeMySQLConverter();
			
		}
		
	}
	public List<ApptType> getApptTypelList() throws DBException{
		return apptTypeData.getAll();
	}


}
