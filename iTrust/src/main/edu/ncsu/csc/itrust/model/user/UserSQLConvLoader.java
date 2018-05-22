package edu.ncsu.csc.itrust.model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.old.enums.Role;

public class UserSQLConvLoader implements SQLLoader<User> {


	
	
	@Override
	public List<User> loadList(ResultSet rs) throws SQLException {
		List<User> list  = new ArrayList<User>();
		while (rs.next())
			list.add(loadSingle(rs));
		return list;
	}

	@Override
	public User loadSingle(ResultSet rs) throws SQLException {
		User ret = new User();
		long mid = rs.getLong("MID");
		String roleName = rs.getString("Role");
		Role userRole = Role.parse(roleName);
		String fn = rs.getString("firstName");
		String ln = rs.getString("lastName");
		//Tester was originally nameless so we'll leave him/her that way
		if(!(userRole.equals(Role.TESTER))){
			ret.setFirstName(fn);
			ret.setLastName(ln);
		}
		ret.setRole(userRole);
		try {
			ret.setMID(mid);
		} catch (ITrustException e) {
			throw new SQLException("Incorrect value for MID stored in MySQL database");
		}
		return ret;
	}

	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, User insertObject,
			boolean newInstance) throws SQLException {
				return null;
	}

}
