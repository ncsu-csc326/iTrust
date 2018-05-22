package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.BillingBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.BillingDAO;

/**
 * VerifyClaimAction handles the interaction between a user and the
 * DAOs. 
 */
public class VerifyClaimAction {
	/**The DAO to access the billing table*/
	private BillingDAO billAccess;
	/**The bill that we are verifying*/
	private BillingBean bill;
	
	/**
	 * VerifyClaimAction simply initializes the instance variables.
	 * @param factory The DAO factory
	 * @param bID The ID of the bill.
	 */
	public VerifyClaimAction(DAOFactory factory, long bID){
		billAccess = factory.getBillingDAO();
		try {
			bill = billAccess.getBillId(bID);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getBill returns the bill we are handling.
	 * @return The billing bean.
	 */
	public BillingBean getBill(){
		return this.bill;
	}
	
	/**
	 * denyClaim handles the user choosing to deny the claim.
	 */
	public void denyClaim(){
		bill.setStatus("Denied");
		try {
			this.billAccess.editBill(bill);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * approveClaim handles the user choosing to approve the claim.
	 */
	public void approveClaim(){
		bill.setStatus("Approved");
		try {
			this.billAccess.editBill(bill);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
}
