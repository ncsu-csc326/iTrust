package edu.ncsu.csc.itrust.model.diagnosis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.icdcode.ICDCode;

public class DiagnosisSQLLoader implements SQLLoader<Diagnosis> {

	@Override
	public List<Diagnosis> loadList(ResultSet rs) throws SQLException {
		List<Diagnosis> list = new LinkedList<Diagnosis>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public Diagnosis loadSingle(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		long visitId = rs.getLong("visitId");
		String icdCode = rs.getString("icdCode");
		String name = rs.getString("name");
		boolean isChronic = rs.getBoolean("is_chronic");
		return new Diagnosis(id, visitId, new ICDCode(icdCode, name, isChronic));
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Diagnosis insertObject,
			boolean newInstance) throws SQLException {
		if (newInstance) {
			ps = conn.prepareStatement("INSERT INTO diagnosis (visitId, icdCode) values (?, ?)");
			ps.setLong(1, insertObject.getVisitId());
			ps.setString(2, insertObject.getCode());
		} else {
			ps = conn.prepareStatement("UPDATE diagnosis SET icdCode = ?, visitId = ? WHERE id = ?");
			ps.setString(1, insertObject.getCode());
			ps.setLong(2, insertObject.getVisitId());
			ps.setLong(3, insertObject.getId());
		}
		return ps;
	}

}
