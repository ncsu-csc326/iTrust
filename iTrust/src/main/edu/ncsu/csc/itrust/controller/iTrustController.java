package edu.ncsu.csc.itrust.controller;

import javax.faces.application.FacesMessage.Severity;

import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

/**
 * Base class for all controllers in iTrust containing functionality universally
 * applicable to controllers.
 * 
 * @author mwreesjo
 */
public class iTrustController {

	private SessionUtils sessionUtils;
	private TransactionLogger logger;

	/**
	 * A fallback error message to be displayed when something goes unexpectedly
	 * wrong (for example, an Exception from an unknown source is thrown).
	 */
	protected static final String GENERIC_ERROR = "Something went wrong and the action couldn't be completed.";

	public iTrustController() {
		this(null, null);
	}

	/**
	 * Initializes iTrustController with SessionUtils and TransactionLogger
	 * instances. Initializes sessionUtils and logger if they are null.
	 * 
	 * @param sessionUtils
	 * @param logger
	 */
	public iTrustController(SessionUtils sessionUtils, TransactionLogger logger) {
		if (sessionUtils == null) {
			sessionUtils = SessionUtils.getInstance();
		}
		if (logger == null) {
			logger = TransactionLogger.getInstance();
		}
		setSessionUtils(sessionUtils);
		setTransactionLogger(logger);
	}

	protected TransactionLogger getTransactionLogger() {
		return logger;
	}

	public void setTransactionLogger(TransactionLogger logger) {
		this.logger = logger;
	}

	protected SessionUtils getSessionUtils() {
		return sessionUtils;
	}

	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	/**
	 * @see {@link SessionUtils#printFacesMessage(Severity, String, String, String)}
	 */
	public void printFacesMessage(Severity severity, String summary, String detail, String clientId) {
		sessionUtils.printFacesMessage(severity, summary, detail, clientId);
	}

	/**
	 * @see {@link TransactionLogger#logTransaction(TransactionType, long, long, String)}
	 */
	public void logTransaction(TransactionType type, Long loggedInMID, Long secondaryMID, String addedInfo) {
		TransactionLogger.getInstance().logTransaction(type, loggedInMID, secondaryMID, addedInfo);
	}

	/**
	 * Logs the TransactionType and added info with current loggedInMID and
	 * current patientMID.
	 */
	public void logTransaction(TransactionType type, String addedInfo) {
		Long loggedInMID = sessionUtils.getSessionLoggedInMIDLong();
		Long patientMID = sessionUtils.getCurrentPatientMIDLong();
		logTransaction(type, loggedInMID, patientMID, addedInfo);
	}
}
