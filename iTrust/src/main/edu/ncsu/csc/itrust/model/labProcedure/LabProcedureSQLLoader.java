package edu.ncsu.csc.itrust.model.labProcedure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class LabProcedureSQLLoader implements SQLLoader<LabProcedure> {

	/** Lab Procedure table name */
	private static final String LAB_PROCEDURE_TABLE_NAME = "labProcedure";

	/** Database column names */
	private static final String COMMENTARY = "commentary";
	private static final String LAB_PROCEDURE_ID = "labProcedureID";
	private static final String LAB_TECHNICIAN_ID = "labTechnicianID";
	private static final String OFFICE_VISIT_ID = "officeVisitID";
	private static final String LAB_PROCEDURE_CODE = "labProcedureCode";
	private static final String PRIORITY = "priority";
	private static final String RESULTS = "results";
	private static final String IS_RESTRICTED = "isRestricted";
	private static final String STATUS = "status";
	private static final String UPDATED_DATE = "updatedDate";
	private static final String CONFIDENCE_INTERVAL_LOWER = "confidenceIntervalLower";
	private static final String CONFIDENCE_INTERVAL_UPPER = "confidenceIntervalUpper";
	private static final String HCP_MID = "hcpMID";

	/** SQL statements relating to lab procedures */
	private static final String INSERT = "INSERT INTO " + LAB_PROCEDURE_TABLE_NAME + " (" 
			+ COMMENTARY + ", "
			+ LAB_TECHNICIAN_ID + ", "
			+ OFFICE_VISIT_ID + ", "
			+ LAB_PROCEDURE_CODE + ", "
			+ PRIORITY + ", "
			+ RESULTS + ", "
			+ IS_RESTRICTED + ", "
			+ STATUS + ", "
			+ CONFIDENCE_INTERVAL_LOWER + ", "
			+ CONFIDENCE_INTERVAL_UPPER + ", "
			+ HCP_MID + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private static final String UPDATE = "UPDATE " + LAB_PROCEDURE_TABLE_NAME + " SET "
			+ COMMENTARY + "=?, "
			+ LAB_TECHNICIAN_ID + "=?, "
			+ OFFICE_VISIT_ID + "=?, "
			+ LAB_PROCEDURE_CODE + "=?, "
			+ PRIORITY + "=?, "
			+ RESULTS + "=?, "
			+ IS_RESTRICTED + "=?, "
			+ STATUS + "=?, "
			+ CONFIDENCE_INTERVAL_LOWER + "=?, "
			+ CONFIDENCE_INTERVAL_UPPER + "=?, "
			+ HCP_MID + "=? WHERE " + LAB_PROCEDURE_ID + "=?;";

	public static final String SELECT_BY_LAB_PROCEDURE = "SELECT * from " + LAB_PROCEDURE_TABLE_NAME + " WHERE "
			+ LAB_PROCEDURE_ID + "=?;";

	public static final String SELECT_BY_LAB_TECHNICIAN = "SELECT * from " + LAB_PROCEDURE_TABLE_NAME + " WHERE "
			+ LAB_TECHNICIAN_ID + "=?;";

	public static final String SELECT_BY_OFFICE_VISIT = "SELECT * from " + LAB_PROCEDURE_TABLE_NAME + " WHERE "
			+ OFFICE_VISIT_ID + "=?;";

	public static final String SELECT_ALL = "SELECT * from " + LAB_PROCEDURE_TABLE_NAME + ";";

	public static final String REMOVE_BY_LAB_PROCEDURE = "DELETE FROM " + LAB_PROCEDURE_TABLE_NAME + " WHERE "
			+ LAB_PROCEDURE_ID + "=?;";

	@Override
	public List<LabProcedure> loadList(ResultSet rs) throws SQLException {
		List<LabProcedure> list = new ArrayList<LabProcedure>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	@Override
	public LabProcedure loadSingle(ResultSet rs) throws SQLException {
		LabProcedure labProcedure = new LabProcedure();

		labProcedure.setLabProcedureID(rs.getLong(LAB_PROCEDURE_ID));
		labProcedure.setCommentary(rs.getString(COMMENTARY));
		labProcedure.setLabTechnicianID(rs.getLong(LAB_TECHNICIAN_ID));
		labProcedure.setOfficeVisitID(rs.getLong(OFFICE_VISIT_ID));
		labProcedure.setLabProcedureCode(rs.getString(LAB_PROCEDURE_CODE));
		labProcedure.setPriority(rs.getInt(PRIORITY));
		labProcedure.setResults(rs.getString(RESULTS));
		labProcedure.setIsRestricted(rs.getBoolean(IS_RESTRICTED));
		labProcedure.setStatus(rs.getLong(STATUS));
		labProcedure.setUpdatedDate(rs.getTimestamp(UPDATED_DATE));
		labProcedure.setConfidenceIntervalLower(rs.getInt(CONFIDENCE_INTERVAL_LOWER));
		labProcedure.setConfidenceIntervalUpper(rs.getInt(CONFIDENCE_INTERVAL_UPPER));
		labProcedure.setHcpMID(rs.getLong(HCP_MID));

		return labProcedure;
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, LabProcedure labProcedure,
			boolean newInstance) throws SQLException {
		StringBuilder query = new StringBuilder(newInstance ? INSERT : UPDATE);
		ps = conn.prepareStatement(query.toString());

		ps.setString(1, labProcedure.getCommentary());
		ps.setLong(2, labProcedure.getLabTechnicianID());
		ps.setLong(3, labProcedure.getOfficeVisitID());
		ps.setString(4, labProcedure.getLabProcedureCode());
		ps.setInt(5, labProcedure.getPriority());
		ps.setString(6, labProcedure.getResults());
		ps.setBoolean(7, labProcedure.isRestricted());
		ps.setLong(8, labProcedure.getStatus().getID());
		setIntOrNull(ps, 9, labProcedure.getConfidenceIntervalLower());
		setIntOrNull(ps, 10, labProcedure.getConfidenceIntervalUpper());
		ps.setLong(11, labProcedure.getHcpMID());

		if (!newInstance) {
			ps.setLong(12, labProcedure.getLabProcedureID());
		}

		return ps;
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
}
