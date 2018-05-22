package edu.ncsu.csc.itrust.model.cptcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class CPTCodeMySQL {

	private CPTCodeValidator validator;
	private DataSource ds;

	/**
	 * Constructor of ImmunizationsMySQL instance.
	 * 
	 * @throws DBException when data source cannot be created from the given context names
	 */
	public CPTCodeMySQL() throws DBException {
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new CPTCodeValidator();
	}
	
	/**
	 * Constructor for testing.
	 * 
	 * @param ds testing data source
	 */
	public CPTCodeMySQL(DataSource ds) {
		this.ds = ds;
		validator = new CPTCodeValidator();
	}
	
	public List<CPTCode> getAll() throws SQLException {
	    try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetAllPreparedStatement(conn);
                ResultSet rs = pstring.executeQuery()){
            return loadResults(rs);
        }
	}

	private PreparedStatement createGetAllPreparedStatement(Connection conn) throws SQLException {
	    PreparedStatement pstring = conn.prepareStatement("SELECT * FROM cptCode");
        return pstring;
    }

    private List<CPTCode> loadResults(ResultSet rs) throws SQLException {
	    List<CPTCode> cptList = new ArrayList<>();
        while (rs.next()){
            String newCode = rs.getString("code");
            String newName = rs.getString("name");
            cptList.add(new CPTCode(newCode, newName));
        }
        return cptList;
    }

    /**
     * Gets a single CPTCode from the database given its code
     * @param code The code we should get
     * @return The code if it exists in the database, null if it does not exist
     * @throws SQLException
     */
    public CPTCode getByCode(String code) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetByCodePreparedStatement(conn, code);
                ResultSet rs = pstring.executeQuery()){
            return loadOneResultIfExists(rs);
        }
	}

	private CPTCode loadOneResultIfExists(ResultSet rs) throws SQLException {
        CPTCode returnCode = null;
        if (rs.next()){
            returnCode = new CPTCode(rs.getString("Code"), rs.getString("name"));
        }
        return returnCode;
    }

    private PreparedStatement createGetByCodePreparedStatement(Connection conn, String code) throws SQLException {
	    PreparedStatement pstring = conn.prepareStatement("SELECT * FROM cptCode WHERE Code=?");
	    pstring.setString(1, code);
        return pstring;
    }

    public boolean add(CPTCode addObj) throws SQLException, FormValidationException {
        validator.validate(addObj);
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createAddPreparedStatement(conn, addObj);){
            return pstring.executeUpdate() > 0;
        } catch (MySQLIntegrityConstraintViolationException e){
            return false;
        }
	}

	private PreparedStatement createAddPreparedStatement(Connection conn, CPTCode addObj) throws SQLException {
	    PreparedStatement pstring = conn.prepareStatement("INSERT INTO cptCode(Code, name) "
                + "VALUES(?, ?)");
        pstring.setString(1, addObj.getCode());
        pstring.setString(2, addObj.getName());
        return pstring;
    }

    public boolean update(CPTCode updateObj) throws SQLException, FormValidationException {
        validator.validate(updateObj);
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createUpdatePreparedStatement(conn, updateObj);){
            return pstring.executeUpdate() > 0;
        }
	}

    private PreparedStatement createUpdatePreparedStatement(Connection conn, CPTCode updateObj) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("UPDATE cptCode SET name=? WHERE code=?");
        pstring.setString(1, updateObj.getName());
        pstring.setString(2, updateObj.getCode());
        return pstring;
    }
    
    public boolean delete(CPTCode deleteObj) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createDeletePreparedStatement(conn, deleteObj);){
            return pstring.executeUpdate() > 0;
        }
    }

    private PreparedStatement createDeletePreparedStatement(Connection conn, CPTCode deleteObj) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM cptCode WHERE code=?");
        pstring.setString(1, deleteObj.getCode());
        return pstring;
    }
    
    public List<CPTCode> getCodesWithFilter(String filter) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = creategetCodesWithFilterPreparedStatement(conn, filter);
                ResultSet rs = pstring.executeQuery()){
            return loadResults(rs);
        }
    }

    private PreparedStatement creategetCodesWithFilterPreparedStatement(Connection conn, String filter) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM cptCode WHERE Code LIKE ?");
        pstring.setString(1, "%" + filter + "%");
        return pstring;
    }
	
}
