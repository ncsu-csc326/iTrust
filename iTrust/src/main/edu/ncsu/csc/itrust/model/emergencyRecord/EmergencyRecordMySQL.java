package edu.ncsu.csc.itrust.model.emergencyRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.prescription.PrescriptionMySQL;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisData;
import edu.ncsu.csc.itrust.model.diagnosis.DiagnosisMySQL;
import edu.ncsu.csc.itrust.model.immunization.ImmunizationMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AllergyDAO;

public class EmergencyRecordMySQL {
    private DataSource ds;
    private PrescriptionMySQL prescriptionLoader;
    private DiagnosisData diagnosisData;
    private AllergyDAO allergyData;
    private ImmunizationMySQL immunizationData;
    
    /**
     * Standard constructor for use in deployment
     * @throws DBException
     */
    public EmergencyRecordMySQL() throws DBException {
        try {
            this.ds = getDataSource();
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        diagnosisData = new DiagnosisMySQL(ds);
        allergyData = DAOFactory.getProductionInstance().getAllergyDAO();
        prescriptionLoader = new PrescriptionMySQL(ds);
        immunizationData = new ImmunizationMySQL(ds);
    }
    
    protected DataSource getDataSource() throws NamingException {
    	Context ctx = new InitialContext();
    	return ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
    }
    
    /**
     * Constructor for testing purposes
     * @param ds The DataSource to use
     * @param allergyData the AllergyDAO to use
     */
    public EmergencyRecordMySQL(DataSource ds, AllergyDAO allergyData) {
        this.ds = ds;
        diagnosisData = new DiagnosisMySQL(ds);
        this.allergyData = allergyData;
        prescriptionLoader = new PrescriptionMySQL(ds);
        immunizationData = new ImmunizationMySQL(ds);
    }
    
    /**
     * Gets the EmergencyRecord for the patient with the given MID
     * @param patientMID The MID of the patient whose record you want
     * @return The retrieved EmergencyRecord
     * @throws DBException
     */
    public EmergencyRecord getEmergencyRecordForPatient(long patientMID) throws DBException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createPreparedStatement(conn, patientMID);
                ResultSet results = pstring.executeQuery()){
            return loadRecord(results);
        } catch (SQLException e){
            throw new DBException(e);
        }
    }
    
    /**
     * A utility method that uses a ResultSet to load an EmergencyRecord.
     * @param rs The ResultSet to load from
     * @param patientMID The MID of the patient that we're loading results for
     * @return The loaded EmergencyRecord
     * @throws SQLException
     * @throws DBException 
     */
    public EmergencyRecord loadRecord(ResultSet rs) throws SQLException, DBException {
        EmergencyRecord newRecord = new EmergencyRecord();
        if (!rs.next()){
            return null;
        }
        long mid = rs.getLong("MID");
        newRecord.setName(rs.getString("firstName") + " " + rs.getString("lastName"));
        LocalDate now = LocalDate.now();
        LocalDate birthdate = rs.getDate("DateOfBirth").toLocalDate();
        int age = (int) ChronoUnit.YEARS.between(birthdate, now);
        newRecord.setAge(age);
        newRecord.setGender(rs.getString("Gender"));
        newRecord.setContactName(rs.getString("eName"));
        newRecord.setContactPhone(rs.getString("ePhone"));
        newRecord.setBloodType(rs.getString("BloodType"));

        LocalDate endDate = LocalDate.now().minusDays(91);
        newRecord.setPrescriptions(
                prescriptionLoader.getPrescriptionsForPatientEndingAfter(
                        mid, endDate));

        newRecord.setAllergies(allergyData.getAllergies(mid));
        newRecord.setDiagnoses(diagnosisData.getAllEmergencyDiagnosis(mid));
        newRecord.setImmunizations(immunizationData.getAllImmunizations(mid));
        return newRecord;
    }
    
    /**
     * A convenience method for preparing the needed SQL query
     * 
     * @param conn The Connection the PreparedStatement should be made on
     * @param mid The mid of the patient whose record we want
     * @return The PreparedStatement for the query
     * @throws SQLException
     */
    private PreparedStatement createPreparedStatement(Connection conn, long mid) throws SQLException{
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM patients WHERE MID=?");
        statement.setLong(1, mid);
        return statement;
    }
}
