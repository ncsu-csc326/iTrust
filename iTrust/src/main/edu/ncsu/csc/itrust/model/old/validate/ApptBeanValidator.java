package edu.ncsu.csc.itrust.model.old.validate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;

public class ApptBeanValidator extends BeanValidator<ApptBean>{

	@Override
	public void validate(ApptBean bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if(bean.getComment() == null)
			return;
		errorList.addIfNotNull(checkFormat("Appointment Comment", bean.getComment(), ValidationFormat.APPT_COMMENT, false));
		LocalDateTime n = LocalDateTime.now();
		LocalDateTime bt = bean.getDate().toLocalDateTime();
//		if(bt.getYear() < n.getYear()){
//			errorList.addIfNotNull("Incorrect Year for Appointment");
//			
//		}
//		else{
//			if((bt.getYear() == n.getYear()) && (bt.getDayOfYear() < n.getDayOfYear())){
//				errorList.addIfNotNull("Incorrect Day of Year for Appointment");
//			}
//		}
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}
