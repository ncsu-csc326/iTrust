package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.RandomPassword;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.model.old.enums.Role;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.AddPersonnelValidator;

/**
 * Used for Add Personnel page (addPersonnel.jsp). This just adds an empty HCP/UAP, creates a random password
 * for them.
 * 
 * Very similar to {@link AddOfficeVisitAction} and {@link AddPatientAction}
 * 
 * Copied from AddHCPAction 
 */

public class AddERespAction {
	private PersonnelDAO personnelDAO;
	private AuthDAO authDAO;
    private long loggedInMID;
	
	/**
 * Sets up the defaults for the class
 * 
 * @param factory factory for creating the defaults.
 * @param loggedInMID person currently logged in 
 */	
	
	public AddERespAction(DAOFactory factory, long loggedInMID) {
		this.personnelDAO = factory.getPersonnelDAO();
		this.loggedInMID = loggedInMID;
		this.authDAO = factory.getAuthDAO();
	}

	/**
	 * Adds the new user.  Event is logged.
	 * 
	 * @param p bean containing the information for the new user
	 * @return MID of the new user.
	 * @throws FormValidationException
	 * @throws ITrustException
	 */
	public long add(PersonnelBean p) throws FormValidationException, ITrustException {
		new AddPersonnelValidator().validate(p);
		long newMID = personnelDAO.addEmptyPersonnel(Role.ER);
		p.setMID(newMID);
		personnelDAO.editPersonnel(p);
		String pwd = authDAO.addUser(newMID, Role.ER, RandomPassword.getRandomPassword());
		p.setPassword(pwd);
		TransactionLogger.getInstance().logTransaction(TransactionType.ER_CREATE, loggedInMID, p.getMID(), "");
		return newMID;
	}
}
