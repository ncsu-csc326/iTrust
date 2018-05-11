package edu.ncsu.csc.itrust.model.loinccode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.SQLLoader;

public class LOINCCodeSQLLoader implements SQLLoader<LOINCCode> {

	@Override
	public List<LOINCCode> loadList(ResultSet rs) throws SQLException {
		ArrayList<LOINCCode> list = new ArrayList<LOINCCode>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public LOINCCode loadSingle(ResultSet rs) throws SQLException {
		String code = rs.getString("code");
		String component = rs.getString("component");
		String kindOfProperty = rs.getString("kind_of_property");
		String timeAspect = rs.getString("time_aspect");
		String system = rs.getString("system");
		String scaleType = rs.getString("scale_type");
		String methodType = rs.getString("method_type");
		return new LOINCCode(code, component, kindOfProperty, timeAspect, system, scaleType, methodType);
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, LOINCCode insertObject,
			boolean newInstance) throws SQLException {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO loinccode (code, component, kind_of_property, time_aspect, system, scale_type, method_type) "
					+ "values (?, ?, ?, ?, ?, ?, ?);";
		} else {
			stmt = "UPDATE loinccode SET "
					+ "code=?, "
					+ "component=?, "
					+ "kind_of_property=?, "
					+ "time_aspect=?, "
					+ "system=?, "
					+ "scale_type=?, "
					+ "method_type=? "
					+ "WHERE code='" + insertObject.getCode() + "';";
		}
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, insertObject.getCode());
		ps.setString(2, insertObject.getComponent());
		ps.setString(3, insertObject.getKindOfProperty());
		ps.setString(4, insertObject.getTimeAspect());
		ps.setString(5, insertObject.getSystem());
		ps.setString(6, insertObject.getScaleType());
		ps.setString(7, insertObject.getMethodType());
		
		return ps;
	}
}
