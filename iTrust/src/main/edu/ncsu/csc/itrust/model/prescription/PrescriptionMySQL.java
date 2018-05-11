package edu.ncsu.csc.itrust.model.prescription;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PatientLoader;

public class PrescriptionMySQL {
    private DataSource ds;
    private PrescriptionValidator validator;
    
    /**
     * Standard constructor for use in deployment
     * @throws DBException
     */
    public PrescriptionMySQL() throws DBException {
        try {
            this.ds = getDataSource();
            this.validator = new PrescriptionValidator();
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
    }
    
    protected DataSource getDataSource() throws NamingException {
    	Context ctx = new InitialContext();
    	return ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
    }
    
    /**
     * Constructor for testing purposes
     * @param ds The DataSource to use
     */
    public PrescriptionMySQL(DataSource ds) {
        this.ds = ds;
        this.validator = new PrescriptionValidator();
    }
    
    /**
     * Gets all prescriptions for the patient with the given MID with ending
     * dates equal to or after the given end date
     * 
     * @param mid The mid of the patient whose prescriptions we should get
     * @param endDate The end date to get prescriptions after
     * @return A List of prescriptions for the given patient that end on or
     *         after the given date.
     * @throws SQLException 
     */
    public List<Prescription> getPrescriptionsForPatientEndingAfter(long mid, LocalDate endDate) throws SQLException{
        
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createERPreparedStatement(conn, mid, endDate);
                ResultSet results = pstring.executeQuery()){
            return loadRecords(results);
        }
    }
    
    /**
     * Adds a Prescription to the database.
     * 
     * @param p The prescription to add
     * @return True if the record was successfully added, false otherwise
     * @throws SQLException 
     */
    public boolean add(Prescription p) throws SQLException {
    	try {
    		validator.validate(p);
    	} catch (FormValidationException e) {
    		throw new SQLException(e.getMessage());
    	}
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createAddPreparedStatement(conn, p);){
            return pstring.executeUpdate() > 0;
        }
    }
    
    public Prescription get(long id) throws SQLException {
    	try (Connection conn = ds.getConnection();
    			PreparedStatement pstring = createGetStatement(conn, id);
    			ResultSet results = pstring.executeQuery()) {
    		List<Prescription> list = loadRecords(results);
    		return list.isEmpty() ? null : list.get(0);
    	}
    }
    
    /**
     * Get all prescription for the patient with the given MID.
     * 
     * @param mid
     * 			MID of patient
     * @return list of MID beloning to the given patient
     * @throws SQLException
     */
    public List<Prescription> getPrescriptionsByMID(long mid) throws SQLException {
    	try (Connection conn = ds.getConnection();
    			PreparedStatement pstring = createGetByMIDStatement(conn, mid);
    			ResultSet results = pstring.executeQuery()) {
    		return loadRecords(results);
    	}
    }
    
    /**
     * Removes a Prescription from the database. It will remove the record
     * with the same id as this one, but will not pay attention to any other
     * fields.
     * @param p The Prescription to remove
     * @return True if the Prescription was successfully removed, false if not
     * @throws SQLException 
     */
    public boolean remove(long id) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createRemovePreparedStatement(conn, id);){
            return pstring.executeUpdate() > 0;
        }
    }
    
    /**
     * Updates a Prescription in the database.
     * @param p The Prescription to update
     * @return True if the Prescription was successfully updated, false if not
     * @throws SQLException 
     */
    public boolean update(Prescription p) throws SQLException {
    	try {
    		validator.validate(p);
    	} catch (FormValidationException e) {
    		throw new SQLException(e.getMessage());
    	}
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createUpdatePreparedStatement(conn, p);){
            return pstring.executeUpdate() > 0;
        }
    }
    
    /**
     * A utility method for creating a PreparedStatement for an update operation
     * @param conn The connection to use
     * @param p The Prescription we're updating
     * @return The generated PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createUpdatePreparedStatement(Connection conn, Prescription p) throws SQLException{
        PreparedStatement pstring = conn.prepareStatement("UPDATE prescription SET patientMID=?, drugCode=?, startDate=?, endDate=?, officeVisitId=?, instructions=?, hcpMID=?, dosage=? WHERE id=?;");
        
        pstring.setLong(1, p.getPatientMID());
        pstring.setString(2, p.getCode());
        pstring.setDate(3, Date.valueOf(p.getStartDate()));
        pstring.setDate(4, Date.valueOf(p.getEndDate()));
        pstring.setLong(5, p.getOfficeVisitId());
        pstring.setString(6, p.getInstructions());
        pstring.setLong(7, p.getHcpMID());
        pstring.setLong(8, p.getDosage());
        pstring.setLong(9, p.getId());
        return pstring;
    }

    /**
     * A utility method for creating a PreparedStatement for a remove operation
     * @param conn The connection to use
     * @param p The Prescription we're removing
     * @return The generated PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createRemovePreparedStatement(Connection conn, long id) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM prescription WHERE id=?");
        pstring.setLong(1, id);
        return pstring;
    }

    /**
     * Gets all prescriptions associated with the given OfficeVisit ID
     * @param officeVisitId The ID of the OfficeVisit we should get Prescriptions for
     * @return A List<Prescription> of all Prescriptions associated with this
     *         OfficeVisit ID
     * @throws SQLException
     */
    public List<Prescription> getPrescriptionsForOfficeVisit(long officeVisitId) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createOVPreparedStatement(conn, officeVisitId);
                ResultSet results = pstring.executeQuery()){
            return loadRecords(results);
        }
    }
    
    /**
     * Gets the list of patient that the user represents
     * @param pid
     * 			id of a representer
     * @return all patients represented by the given representer
     * @throws SQLException
     */
    public List<PatientBean> getListOfRepresentees(long pid) throws SQLException {    
    	try (Connection conn = ds.getConnection();
    			PreparedStatement pstring = createListOfRepsPreparedStatement(conn, pid);
    			ResultSet rs = pstring.executeQuery()){
    		return new PatientLoader().loadList(rs);
    	}
    }
    
    /**
     * Utility method to generate a prepared statement 
     * in order to get the list of representees of a user
     * 
     * @param conn Connection to use
     * @param mid mid of representer
     * @return Prepared statement for getting all representees of a representer
     * @throws SQLException
     */
    private PreparedStatement createListOfRepsPreparedStatement(Connection conn, long mid) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT patients.* FROM representatives, "
        		+ "patients WHERE RepresenterMID=? AND RepresenteeMID=patients.MID");
        pstring.setLong(1, mid);
        return pstring;
    }
    
    
    /**
     * Gets a codes actual name
     * 
     * @param code id to search and get name for
     * @return string representation of the code
     * @throws SQLException
     */
    public String getCodeName(String code) throws SQLException {
    	try (Connection conn = ds.getConnection();
    			PreparedStatement pstring = createGetCodeNamePreparedStatement(conn, code);
    			ResultSet rs = pstring.executeQuery()){
    		
    		if (!rs.next()) 
				return "";

    		return rs.getString("Description");
    	}
    }
    
    /**
     * A utility method for creating a PreparedStatement for 
     * getting a codes actual name
     * 
     * @param conn The Connection to use
     * @param mid The code's unique code to search for
     * 
     * @return A string representation of the code
     * @throws SQLException
     */
    private PreparedStatement createGetCodeNamePreparedStatement(Connection conn, String code) throws SQLException {
    	PreparedStatement pstring = conn.prepareStatement("SELECT a.Description FROM ndcodes a JOIN prescription On a.code=?");
        pstring.setString(1, code);
        return pstring;
    }
    
    /**
     * A utility method that loads all Prescriptions from a ResultSet into a
     * List<Prescription> and returns it.
     * @param rs The ResultSet to load
     * @return A List of all Prescriptions in the ResultSet
     * @throws SQLException
     */
    public List<Prescription> loadRecords(ResultSet rs) throws SQLException{
        List<Prescription> prescriptions = new ArrayList<>();
        while (rs.next()){
            Prescription newP = new Prescription();
            newP.setDrugCode(new MedicationBean(rs.getString("code"), rs.getString("description")));
            newP.setEndDate(rs.getDate("endDate").toLocalDate());
            newP.setStartDate(rs.getDate("startDate").toLocalDate());
            newP.setOfficeVisitId(rs.getLong("officeVisitId"));
            newP.setPatientMID(rs.getLong("patientMID"));
            newP.setId(rs.getLong("id"));
            newP.setInstructions(rs.getString("instructions"));
            newP.setDosage(rs.getLong("dosage"));
            newP.setHcpMID(rs.getLong("hcpMID"));
            prescriptions.add(newP);
        }
        
        return prescriptions;
    }
    
    /**
     * A utility method for creating a PreparedStatement for an EmergencyRecord
     * operation.
     * 
     * @param conn The Connection to use
     * @param mid The MID of the patient we need the records for
     * @param endDate All prescriptions for this patient that end after this
     *                date will be returned.
     * @return A List<Prescription> containing all appropriate Prescriptions for
     *         the EmergencyRecord
     * @throws SQLException
     */
    private PreparedStatement createERPreparedStatement(Connection conn, long mid, LocalDate endDate) throws SQLException{
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM prescription, ndcodes WHERE drugCode = code AND patientMID=? AND endDate>=? ORDER BY endDate DESC");

        pstring.setLong(1, mid);
        pstring.setDate(2, Date.valueOf(endDate));
        return pstring;
    }
    
    /**
     * A utility method for creating a PreparedStatement for an add operation
     * @param conn The Connection to use
     * @param p The Prescription to add
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createAddPreparedStatement(Connection conn, Prescription p) throws SQLException{
        PreparedStatement pstring = conn.prepareStatement("INSERT INTO prescription(patientMID, drugCode, startDate, endDate, officeVisitId, instructions, hcpMID, dosage) "
                 + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
        pstring.setLong(1, p.getPatientMID());
        pstring.setString(2, p.getCode());
        pstring.setDate(3, Date.valueOf(p.getStartDate()));
        pstring.setDate(4, Date.valueOf(p.getEndDate()));
        pstring.setLong(5, p.getOfficeVisitId());
        pstring.setString(6, p.getInstructions());
        pstring.setLong(7, p.getHcpMID());
        pstring.setLong(8, p.getDosage());
        return pstring;
    }
    
    /**
     * A utility method for creating a PreparedStatement for getting all
     * Prescriptions for an office visit
     * @param conn The Connection to use
     * @param officeVisitId The ID of theOfficeVisit to get Prescriptions for
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createOVPreparedStatement(Connection conn, long officeVisitId) throws SQLException{
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM prescription, ndcodes WHERE drugCode = code AND officeVisitId=?");

        pstring.setLong(1, officeVisitId);
        return pstring;
    }
    
    /**
     * Construct a prepared statement to retrieve all prescriptions for a given MID.
     * 
     * @param conn connection to use
     * @param patientMID patient's MID
     * @return PreparedStatement getting all patient's prescriptions
     * @throws SQLException 
     */
    private PreparedStatement createGetByMIDStatement(Connection conn, long patientMID) throws SQLException {
    	PreparedStatement pstring = conn.prepareStatement("SELECT * FROM prescription, ndcodes WHERE drugCode = code AND patientMID=? ORDER BY startDate DESC");
    	pstring.setLong(1,  patientMID);
    	return pstring;
    }
    
    /**
     * Construct a prepared statement to retrieve prescription for a given id.
     * 
     * @param conn connection to use
     * @param id prescription id
     * @return PreparedStatement getting all patient's prescriptions
     * @throws SQLException 
     */
    private PreparedStatement createGetStatement(Connection conn, long id) throws SQLException {
    	PreparedStatement pstring = conn.prepareStatement("SELECT * FROM prescription, ndcodes WHERE drugCode = code AND id=?");
    	pstring.setLong(1,  id);
    	return pstring;
    }
}
