package edu.ncsu.csc.itrust.model.officeVisit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;

public class OfficeVisitSQLLoader implements SQLLoader<OfficeVisit>{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OfficeVisit> loadList(ResultSet rs) throws SQLException {
		ArrayList<OfficeVisit> list = new ArrayList<OfficeVisit>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficeVisit loadSingle(ResultSet rs) throws SQLException {
		OfficeVisit retVisit = new OfficeVisit();
		retVisit.setVisitID(Long.parseLong(rs.getString("visitID")));
		retVisit.setPatientMID(Long.parseLong(rs.getString("patientMID")));
		retVisit.setLocationID(rs.getString("locationID"));
		retVisit.setDate(rs.getTimestamp("visitDate").toLocalDateTime());
		retVisit.setApptTypeID(rs.getLong("apptTypeID"));
		retVisit.setNotes(rs.getString("notes"));
		retVisit.setSendBill(rs.getBoolean("sendBill"));
		
		// Load patient health metrics
		retVisit.setHeight(getFloatOrNull(rs, "height"));
		retVisit.setLength(getFloatOrNull(rs, "length"));
		retVisit.setWeight(getFloatOrNull(rs, "weight"));
		retVisit.setHeadCircumference(getFloatOrNull(rs, "head_circumference"));
		retVisit.setBloodPressure(rs.getString("blood_pressure"));
		retVisit.setHDL(getIntOrNull(rs, "hdl"));
		retVisit.setTriglyceride(getIntOrNull(rs, "triglyceride"));
		retVisit.setLDL(getIntOrNull(rs, "ldl"));
		retVisit.setHouseholdSmokingStatus(getIntOrNull(rs, "household_smoking_status"));
		retVisit.setPatientSmokingStatus(getIntOrNull(rs, "patient_smoking_status"));
		
		return retVisit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, OfficeVisit ov, boolean newInstance)
			throws SQLException {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO officeVisit(patientMID, visitDate, locationID, apptTypeID, notes, sendBill, height, length, weight,"
					+ "head_circumference, blood_pressure, hdl, triglyceride, ldl, household_smoking_status, patient_smoking_status) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		} else {
			long id = ov.getVisitID();
			stmt = "UPDATE officeVisit SET patientMID=?, "
					+ "visitDate=?, "
					+ "locationID=?, "
					+ "apptTypeID=?, "
					+ "notes=?, "
					+ "sendBill=?, "
					+ "height=?, "
					+ "length=?, "
					+ "weight=?, "
					+ "head_circumference=?, "
					+ "blood_pressure=?, "
					+ "hdl=?, "
					+ "triglyceride=?, "
					+ "ldl=?, "
					+ "household_smoking_status=?, "
					+ "patient_smoking_status=? "
					+ "WHERE visitID=" + id + ";";
		}
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, ov.getPatientMID());
		Timestamp ts = Timestamp.valueOf(ov.getDate());

		ps.setTimestamp(2, ts);

		ps.setString(3, ov.getLocationID());
		ps.setLong(4, ov.getApptTypeID());
		String noteText = "";
		if (ov.getNotes() != (null)){
			noteText = ov.getNotes();
		}
		ps.setString(5, noteText);
		boolean bill = false;
		if(ov.getSendBill() != null){
			bill = ov.getSendBill();
		}
		ps.setBoolean(6, bill);
		
		// Patient health metrics
		setFloatOrNull(ps, 7, ov.getHeight());
		setFloatOrNull(ps, 8, ov.getLength());
		setFloatOrNull(ps, 9, ov.getWeight());
		setFloatOrNull(ps, 10, ov.getHeadCircumference());
		ps.setString(11, ov.getBloodPressure());
		setIntOrNull(ps, 12, ov.getHDL());
		setIntOrNull(ps, 13, ov.getTriglyceride());
		setIntOrNull(ps, 14, ov.getLDL());
		
		Integer hss = ov.getHouseholdSmokingStatus();
		if (hss == null) {
			hss = 0;
		}
		ps.setInt(15, hss);
		
		Integer pss = ov.getPatientSmokingStatus();
		if (pss == null) {
			pss = 0;
		}
		ps.setInt(16, pss);

		return ps;
	}
	
	/**
	 * Get the integer value if initialized in DB, otherwise get null.
	 * 
	 * @param rs 
	 * 		ResultSet object
	 * @param field
	 * 		name of DB attribute 
	 * @return Integer value or null
	 * @throws SQLException when field doesn't exist in the result set
	 */
	public Integer getIntOrNull(ResultSet rs, String field) throws SQLException {
		Integer ret = rs.getInt(field);
		if (rs.wasNull()) {
			ret = null;
		}
		return ret;
	}
	
	/**
	 * Get the float value if initialized in DB, otherwise get null.
	 * 
	 * @param rs 
	 * 		ResultSet object
	 * @param field
	 * 		name of DB attribute 
	 * @return Float value or null
	 * @throws SQLException when field doesn't exist in the result set
	 */
	public Float getFloatOrNull(ResultSet rs, String field) throws SQLException {
		Float ret = rs.getFloat(field);
		if (rs.wasNull()) {
			ret = null;
		}
		return ret;
	}
	
	/**
	 * Set integer placeholder in statement to a value or null
	 * 
	 * @param ps
	 * 		PreparedStatement object
	 * @param index
	 * 		Index of placeholder in the prepared statement
	 * @param value
	 * 		Value to set to placeholder, the value may be null 
	 * @throws SQLException
	 * 		When placeholder is invalid
	 */
	public void setIntOrNull(PreparedStatement ps, int index, Integer value) throws SQLException {
		if (value == null) {
			ps.setNull(index, java.sql.Types.INTEGER);
		} else {
			ps.setInt(index, value);
		}
	}
	
	/**
	 * Set float placeholder in statement to a value or null
	 * 
	 * @param ps
	 * 		PreparedStatement object
	 * @param index
	 * 		Index of placeholder in the prepared statement
	 * @param value
	 * 		Value to set to placeholder, the value may be null 
	 * @throws SQLException
	 * 		When placeholder is invalid
	 */
	public void setFloatOrNull(PreparedStatement ps, int index, Float value) throws SQLException {
		if (value == null) {
			ps.setNull(index, java.sql.Types.FLOAT);
		} else {
			ps.setFloat(index, value);
		}
	}

}
