package edu.ncsu.csc.itrust.model.immunization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;

public class ImmunizationSQLLoader implements SQLLoader<Immunization> { 

	@Override
	public List<Immunization> loadList(ResultSet rs) throws SQLException {
		List<Immunization> list = new LinkedList<Immunization>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public Immunization loadSingle(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		long visitId = rs.getLong("visitId");
		String cptCode = rs.getString("cptCode");
		String name = rs.getString("name");
		return new Immunization(id, visitId, new CPTCode(cptCode, name));
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Immunization insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if( newInstance ){ // IS NEW CODE
			stmt = "INSERT INTO immunization(visitId, cptcode) "
					+ "VALUES (?, ?);";
		} else { // NOT NEW
			long id = insertObject.getId();
			stmt = "UPDATE immunization SET  "
					+ "visitId=?, "
					+ "cptcode=? "
					+ "WHERE id=" + id + ";";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong( 1, insertObject.getVisitId() );
		ps.setString( 2,  insertObject.getCptCode().getCode() );
		
		return ps;
	}
}
