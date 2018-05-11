package edu.ncsu.csc.itrust.logger;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * Singleton provider of the transaction logging mechanism.
 * 
 * @author mwreesjo
 *
 */
public class TransactionLogger {

	/** The singleton instance of this class. */
	private static TransactionLogger singleton = null;

	/** The DAO which exposes logging functionality to the singleton */
	TransactionDAO dao;

	private TransactionLogger() {
		dao = DAOFactory.getProductionInstance().getTransactionDAO();
	}

	/**
	 * @return Singleton instance of this transaction logging mechanism.
	 */
	public static synchronized TransactionLogger getInstance() {
		if (singleton == null)
			singleton = new TransactionLogger();
		return singleton;
	}

	/**
	 * Logs a transaction. @see {@link TransactionDAO#logTransaction}
	 */
	public void logTransaction(TransactionType type, Long loggedInMID, Long secondaryMID, String addedInfo) {
		try {
			dao.logTransaction(type, loggedInMID, secondaryMID, addedInfo);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
}