package edu.ncsu.csc.itrust.model.loinccode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class LOINCCodeMySQL implements LOINCCodeData {
	private DataSource ds;
	private LOINCCodeValidator validator;
	private LOINCCodeSQLLoader loader;

	/**
	 * Standard constructor for use in deployment
	 * 
	 * @throws DBException
	 */
	public LOINCCodeMySQL() throws DBException {
		try {
			this.ds = getDataSource();
			this.validator = new LOINCCodeValidator();
			this.loader = new LOINCCodeSQLLoader();
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
	 * 
	 * @param ds
	 *            The DataSource to use
	 */
	public LOINCCodeMySQL(DataSource ds) {
		this.ds = ds;
		this.validator = new LOINCCodeValidator();
		this.loader = new LOINCCodeSQLLoader();
	}

	@Override
	public List<LOINCCode> getAll() throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM loincCode;");
				ResultSet rs = ps.executeQuery();) {
			return loader.loadList(rs);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public LOINCCode getByID(long id) throws DBException {
		// Not applicable for LOINCCode because it doesn't have id
		return null;
	}

	@Override
	public LOINCCode getByCode(String code) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement(String.format("SELECT * FROM loincCode WHERE code='%s';", code));
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				return loader.loadSingle(rs);
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	@Override
	public boolean add(LOINCCode addObj) throws FormValidationException, DBException {
		validator.validate(addObj);
		PreparedStatement pstring = null;
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = loader.loadParameters(conn, pstring, addObj, true);) {
			return ps.executeUpdate() > 0;
		} catch (MySQLIntegrityConstraintViolationException e){
            return false;
        } catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public boolean update(LOINCCode updateObj) throws DBException, FormValidationException {
		validator.validate(updateObj);
		PreparedStatement pstring = null;
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = loader.loadParameters(conn, pstring, updateObj, false);) {
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

    public boolean delete(LOINCCode deleteObj) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createDeletePreparedStatement(conn, deleteObj);){
            return pstring.executeUpdate() > 0;
        }
    }

    private PreparedStatement createDeletePreparedStatement(Connection conn, LOINCCode deleteObj) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM loinccode WHERE code=?");
        pstring.setString(1, deleteObj.getCode());
        return pstring;
    }

    public List<LOINCCode> getCodesWithFilter(String filterString) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = creategetCodesWithFilterPreparedStatement(conn, filterString);
                ResultSet rs = pstring.executeQuery()){
            return loader.loadList(rs);
        }
    }

    private PreparedStatement creategetCodesWithFilterPreparedStatement(Connection conn, String filterString) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM loinccode WHERE code LIKE ?");
        pstring.setString(1, "%" + filterString + "%");
        return pstring;
    }
	
	

}
