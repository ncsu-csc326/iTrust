package edu.ncsu.csc.itrust.model.icdcode;

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

public class ICDCodeMySQL {

    private DataSource ds;
    private ICDCodeValidator validator;
    
    /**
     * Production constructor
     * @throws DBException
     */
    public ICDCodeMySQL() throws DBException{
        validator = new ICDCodeValidator();
        try {
            this.ds = getDataSource();
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
    }
    
    /**
     * Testing constructor
     * @param ds The DataSource to use
     */
    public ICDCodeMySQL(DataSource ds){
        validator = new ICDCodeValidator();
        this.ds = ds;
    }
    
    /**
     * A utility method for getting the DataSource
     * @return The DataSource
     * @throws NamingException
     */
    protected DataSource getDataSource() throws NamingException {
        Context ctx = new InitialContext();
        return ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
    }
    
    /**
     * Gets all ICDCodes in the database
     * @return A List<ICDCode> of all ICDCodes in the database
     * @throws DBException
     * @throws SQLException
     */
	public List<ICDCode> getAll() throws SQLException {
	    try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetAllPreparedStatement(conn);
                ResultSet rs = pstring.executeQuery()){
            return loadResults(rs);
        }
	}

	/**
	 * A utility method for loading results from a ResultSet
	 * @param rs The ResultSet to load from
	 * @return A List<ICDCode> of all ICDCodes in the ResultSet
	 * @throws SQLException
	 */
	private List<ICDCode> loadResults(ResultSet rs) throws SQLException {
        List<ICDCode> icdList = new ArrayList<>();
        while (rs.next()){
            String newCode = rs.getString("code");
            String newName = rs.getString("name");
            boolean newChronic = rs.getBoolean("is_chronic");
            icdList.add(new ICDCode(newCode, newName, newChronic));
        }
        return icdList;
    }

	/**
	 * Creates the PreparedStatement for the getAll() method
	 * @param conn The Connection to use
	 * @return The new PreparedStatement
	 * @throws SQLException
	 */
    private PreparedStatement createGetAllPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM icdCode");
        return pstring;
    }
    
    /**
     * Gets the given ICDCode
     * @param code The code to get
     * @return The ICDCode
     * @throws SQLException
     */
    public ICDCode getByCode(String code) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = creategetByCodePreparedStatement(conn, code);
                ResultSet rs = pstring.executeQuery()){
            return loadOneResult(rs);
        }
    }

    /**
     * A utility method for loading a single ICDCode from a ResultSet
     * @param rs The ResultSet to load from
     * @return The first ICDCode from the ResultSet if present, null if
     *         ResultSet is empty.
     * @throws SQLException
     */
    private ICDCode loadOneResult(ResultSet rs) throws SQLException {
        ICDCode loadedCode = null;
        if (rs.next()){
            loadedCode = new ICDCode(rs.getString("code"), rs.getString("name"), rs.getBoolean("is_chronic"));
        }
        return loadedCode;
    }

    /**
     * Creates the PreparedStatement for the getByCode() method
     * @param conn The Connection to use
     * @param code The code to get
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement creategetByCodePreparedStatement(Connection conn, String code) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM icdCode WHERE code=?");
        pstring.setString(1, code);
        return pstring;
    }

    /**
     * Adds an ICDCode to the database
     * @param addObj The ICDCode to add
     * @return True if the record was added successfully, false otherwise
     * @throws SQLException If there was a database error
     * @throws FormValidationException If there was a validation error
     */
	public boolean add(ICDCode addObj) throws SQLException, FormValidationException {
	    validator.validate(addObj);
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createAddPreparedStatement(conn, addObj);){
            return pstring.executeUpdate() > 0;
        } catch (MySQLIntegrityConstraintViolationException e){
            return false;
        }
	}

	/**
	 * Creates the PreparedStatement for the add() method
	 * @param conn The Connection to use
	 * @param addObj The ICDCode to add
	 * @return The new PreparedStatement
	 * @throws SQLException
	 */
	private PreparedStatement createAddPreparedStatement(Connection conn, ICDCode addObj) throws SQLException {
	    PreparedStatement pstring = conn.prepareStatement("INSERT INTO icdCode(code, name, is_chronic) "
                + "VALUES(?, ?, ?)");
        pstring.setString(1, addObj.getCode());
        pstring.setString(2, addObj.getName());
        pstring.setBoolean(3, addObj.isChronic());
        return pstring;
    }

	/**
	 * Updates an existing ICDCode in the database
	 * @param updateObj The ICDCode to update. NOTE: this object MUST have the
	 *                  code of the ICDCode that you wish to update. Updating
	 *                  the code requires deleting the record from the database
	 *                  and adding a new one with an updated code.
	 * @return True if successfully updated, false otherwise
     * @throws SQLException If there was a database error
     * @throws FormValidationException If there was a validation error
	 */
    public boolean update(ICDCode updateObj) throws SQLException, FormValidationException {
        validator.validate(updateObj);
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createUpdatePreparedStatement(conn, updateObj);){
            return pstring.executeUpdate() > 0;
        }
	}
	
    /**
     * Creates the PreparedStatement for the update() method
     * @param conn The Connection to use
     * @param updateObj The ICDCode to update
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createUpdatePreparedStatement(Connection conn, ICDCode updateObj) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("UPDATE icdCode SET name=?, is_chronic=? WHERE code=?");
        pstring.setString(1, updateObj.getName());
        pstring.setBoolean(2, updateObj.isChronic());
        pstring.setString(3, updateObj.getCode());
        return pstring;
    }

    /**
     * Deletes an ICDCode from the database
     * @param deleteObj The ICDCode to delete
     * @return True if the deletion was successful, false otherwise
     * @throws SQLException
     */
    public boolean delete(ICDCode deleteObj) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createDeletePreparedStatement(conn, deleteObj);){
            return pstring.executeUpdate() > 0;
        }
    }

    /**
     * Creates the PreparedStatement for the delete() method
     * @param conn The Connection to use
     * @param deleteObj The ICDCode to delete
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createDeletePreparedStatement(Connection conn, ICDCode deleteObj) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM icdCode WHERE code=?");
        pstring.setString(1, deleteObj.getCode());
        return pstring;
    }

    public List<ICDCode> getCodesWithFilter(String filterString) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = creategetCodesWithFilterPreparedStatement(conn, filterString);
                ResultSet rs = pstring.executeQuery()){
            return loadResults(rs);
        }
    }

    private PreparedStatement creategetCodesWithFilterPreparedStatement(Connection conn, String filterString) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM icdCode WHERE code LIKE ?");
        pstring.setString(1, "%" + filterString + "%");
        return pstring;
    }
}
