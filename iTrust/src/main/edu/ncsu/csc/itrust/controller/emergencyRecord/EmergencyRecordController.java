package edu.ncsu.csc.itrust.controller.emergencyRecord;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecord;
import edu.ncsu.csc.itrust.model.emergencyRecord.EmergencyRecordMySQL;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * A controller class for EmergencyRecord
 * @author nmiles
 *
 */
@ManagedBean(name="emergency_record_controller")
@SessionScoped
public class EmergencyRecordController extends iTrustController {
    private EmergencyRecordMySQL sql;
    
    /**
     * Constructs a new EmergencyRecordController
     * @throws DBException 
     */
    public EmergencyRecordController() throws DBException{
        sql = new EmergencyRecordMySQL();
    }
    /**
     * Constructor for testing purposes.
     * 
     * @param ds The DataSource to use
     * @param allergyData the AllergyDAO to use
     */
    public EmergencyRecordController(DataSource ds, AllergyDAO allergyData) throws DBException{
    	EmergencyRecordMySQL sql;
    	sql = new EmergencyRecordMySQL(ds, allergyData);
    	this.sql = sql;
    }
    
    /**
     * Loads the appropriate data for an EmergencyRecord for the given MID.
     * The loaded record is returned, but it is also stored for later retrieval
     * with getRecord(). This method MUST be called before calling getRecord().
     * 
     * @param mid The mid of the patient to load the record for
     * @return The loaded EmergencyRecord if loaded successfully, null if
     *         loading failed
     */
    public EmergencyRecord loadRecord(String midString){
    	long mid;
    	EmergencyRecord record;
        try {
        	mid = Long.parseLong(midString);
        	record = sql.getEmergencyRecordForPatient(mid);
        } catch (Exception e) {
            return null;
        }
        return record;
    }
    
    /**
	 * Logs that the emergency record has been viewed by the logged in MID for
	 * the current patient MID. This method should be invoked once per page
	 * view.
	 */
	public void logViewEmergencyRecord() {
		// If the current patient MID is null, then no patient has been
		// selected, and there's no ER to display.
		if (getSessionUtils().getCurrentPatientMID() != null) {
			logTransaction(TransactionType.EMERGENCY_REPORT_VIEW, null);
		}
	}
}
