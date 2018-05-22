package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.action.base.PersonnelBaseAction;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.PersonnelValidator;

/**
 * Edits the designated personnel Used by admin/editPersonnel.jsp, staff/editMyDemographics.jsp,
 * editPersonnel.jsp
 * 
 * 
 */
public class EditPersonnelAction extends PersonnelBaseAction {
	private PersonnelDAO personnelDAO;
	private AuthDAO authDAO;
	private PersonnelValidator validator = new PersonnelValidator();;

	/**
	 * Super class validates the patient id
	 * 
	 * @param factory The DAOFactory used to create the DAOs for this action.
	 * @param loggedInMID The MID of the user editing this personnel.
	 * @param pidString The ID of the user being edited.
	 * @throws ITrustException
	 */
	public EditPersonnelAction(DAOFactory factory, long loggedInMID, String pidString) throws ITrustException {
		super(factory, pidString);

		this.authDAO = factory.getAuthDAO();
		long pidlong = Long.parseLong(pidString);
		Role editor = authDAO.getUserRole(loggedInMID);
		Role editing = authDAO.getUserRole(pidlong);

		if (editor == editing && pidlong != loggedInMID){
			throw new ITrustException("You can only edit your own demographics!");
		}else if (editor == Role.HCP && editing == Role.ADMIN || editor == Role.UAP && editing == Role.HCP
				|| editor == Role.ADMIN && editing == Role.UAP){
			throw new ITrustException("You are not authorized to edit this record!");
		}
		this.personnelDAO = factory.getPersonnelDAO();
		TransactionLogger.getInstance().logTransaction(TransactionType.PERSONNEL_VIEW, loggedInMID , pidlong, editing.getUserRolesString());
	}

	/**
	 * Takes information from the personnelForm param and updates the patient
	 * 
	 * @param personnelForm
	 *            PersonnelBean with new information
	 * @throws ITrustException
	 * @throws FormValidationException
	 */
	public void updateInformation(PersonnelBean personnelForm, long loggedInMID) throws ITrustException,
			FormValidationException {
		personnelForm.setMID(pid);
		validator.validate(personnelForm);
		personnelDAO.editPersonnel(personnelForm);
		
		if(personnelForm.getRole() == Role.HCP) // If pid belongs to an HCP
		    TransactionLogger.getInstance().logTransaction(TransactionType.LHCP_EDIT, loggedInMID , personnelForm.getMID(), "");
		else if(personnelForm.getRole() == Role.UAP) // If pid belongs to a UAP
            TransactionLogger.getInstance().logTransaction(TransactionType.UAP_EDIT, loggedInMID, personnelForm.getMID(), "");
        else if(personnelForm.getRole() == Role.ER) // If pid belongs to a ER
            TransactionLogger.getInstance().logTransaction(TransactionType.ER_EDIT, loggedInMID, personnelForm.getMID(), "");
        else if(personnelForm.getRole() == Role.PHA) // If pid belongs to a PHA
            TransactionLogger.getInstance().logTransaction(TransactionType.PHA_EDIT, loggedInMID, personnelForm.getMID(), "");
        else if(personnelForm.getRole() == Role.LT) // If pid belongs to a LT
            TransactionLogger.getInstance().logTransaction(TransactionType.LT_EDIT, loggedInMID, personnelForm.getMID(), "");
		
	}
	
}
