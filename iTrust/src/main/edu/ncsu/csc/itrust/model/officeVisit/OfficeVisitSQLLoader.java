package edu.ncsu.csc.itrust.model.officeVisit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;

public class OfficeVisitSQLLoader implements SQLLoader<OfficeVisit>{

	@Override
	public List<OfficeVisit> loadList(ResultSet rs) throws SQLException {
		ArrayList<OfficeVisit> list = new ArrayList<OfficeVisit>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;

	}

	@Override
	public OfficeVisit loadSingle(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		
		OfficeVisit retVisit = new OfficeVisit();
		retVisit.setVisitID(Long.parseLong(rs.getString("visitID")));
		retVisit.setPatientMID(Long.parseLong(rs.getString("patientMID")));
		retVisit.setLocationID(rs.getString("locationID"));
		retVisit.setDate(rs.getTimestamp("visitDate").toLocalDateTime());
		retVisit.setApptTypeID(rs.getLong("apptTypeID"));
		retVisit.setNotes(rs.getString("notes"));
		retVisit.setSendBill(rs.getBoolean("sendBill"));
		return retVisit;
	}
	
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, OfficeVisit ov, boolean newInstance)
			throws SQLException {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO officeVisit(patientMID, visitDate, locationID, apptTypeID, notes, sendBill) "
					+ "VALUES (? ,?, ?, ?, ?, ?);";

		} else {
			long id = ov.getVisitID();
			stmt = "UPDATE officeVisit SET patientMID=?, "
					+ "visitDate=?, "
					+ "locationID=?, "
					+ "apptTypeID=?, "
					+ "notes=?, "
					+ "sendBill=? "
					+ "WHERE visitID=" + id + ";";
		}
		ps = conn.prepareStatement(stmt);
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

		return ps;
	}

}
