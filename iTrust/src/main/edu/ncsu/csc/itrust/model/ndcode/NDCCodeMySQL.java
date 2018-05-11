package edu.ncsu.csc.itrust.model.ndcode;

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

public class NDCCodeMySQL {
    private DataSource ds;
    NDCCodeValidator validator;
    
    /**
     * Standard constructor for use in deployment
     * @throws DBException
     */
    public NDCCodeMySQL() throws DBException {
        validator = new NDCCodeValidator();
        try {
            this.ds = getDataSource();
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
     * @param ds The DataSource to use
     */
    public NDCCodeMySQL(DataSource ds) {
        validator = new NDCCodeValidator();
        this.ds = ds;
    }
    
    /**
     * Adds an NDCode to the database
     * 
     * @param nd The NDCCode to add
     * @return True if the NDCCode was successfully added
     * @throws FormValidationException
     * @throws SQLException
     */
    public boolean add(NDCCode nd) throws FormValidationException, SQLException{
        validator.validate(nd);
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createAddPreparedStatement(conn, nd);){
            return pstring.executeUpdate() > 0;
        } catch (MySQLIntegrityConstraintViolationException e){
            return false;
        }
    }

    /**
     * A utility method used to create a PreparedStatement for the add() method
     * 
     * @param conn The Connection to use
     * @param nd The NDCCode that we're adding
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createAddPreparedStatement(Connection conn, NDCCode nd) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("INSERT INTO ndcodes(Code, Description) "
                + "VALUES(?, ?)");
        pstring.setString(1, nd.getCode());
        pstring.setString(2, nd.getDescription());
        return pstring;
    }
    
    /**
     * Deletes an NDCCode from the database
     * @param nd The NDCCode to delete
     * @return True if the record was successfully deleted
     * @throws SQLException
     */
    public boolean delete(NDCCode nd) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createDeletePreparedStatement(conn, nd);){
            return pstring.executeUpdate() > 0;
        }
    }
    
    /**
     * A utility method used to create a PreparedStatement for the delete() method
     * 
     * @param conn The Connection to use
     * @param nd The NDCCode that we're deleting
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createDeletePreparedStatement(Connection conn, NDCCode nd) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM ndcodes WHERE Code=?");
        pstring.setString(1, nd.getCode());
        return pstring;
    }
    
    /**
     * Gets all NDCCode in the database
     * @return A List<NDCCode> containing all NDCCode in the database
     * @throws SQLException
     */
    public List<NDCCode> getAll() throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetAllPreparedStatement(conn);
                ResultSet rs = pstring.executeQuery()){
            return loadResults(rs);
        }
    }

    /**
     * A utility method used to create a PreparedStatement for the getAll() method
     * 
     * @param conn The Connection to use
     * @return The new PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement createGetAllPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM ndcodes");
        return pstring;
    }

    /**
     * A utility method that loads all NDCCodes in a ResultSet
     * 
     * @param rs The ResultSet to load from
     * @return A List<NDCCode> of all NDCCodes from the ResultSet
     * @throws SQLException
     */
    private List<NDCCode> loadResults(ResultSet rs) throws SQLException {
        List<NDCCode> codes = new ArrayList<>();
        while (rs.next()){
            NDCCode newNDCode = new NDCCode();
            newNDCode.setCode(rs.getString("Code"));
            newNDCode.setDescription(rs.getString("Description"));
            codes.add(newNDCode);
        }
        return codes;
    }
    
    public NDCCode getByCode(String code) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetByCodePreparedStatement(conn, code);
                ResultSet rs = pstring.executeQuery()){
            return loadOneResult(rs);
        }
    }

    private PreparedStatement createGetByCodePreparedStatement(Connection conn, String code) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM ndcodes WHERE Code=?");
        pstring.setString(1, code);
        return pstring;
    }

    private NDCCode loadOneResult(ResultSet rs) throws SQLException {
        NDCCode code = null;
        if (rs.next()){
            code = new NDCCode();
            code.setCode(rs.getString("Code"));
            code.setDescription(rs.getString("Description"));
        }
        return code;
    }
    
    public boolean update(NDCCode toChange) throws FormValidationException, SQLException{
        validator.validate(toChange);
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createUpdatePreparedStatement(conn, toChange);){
            return pstring.executeUpdate() > 0;
        }
    }

    private PreparedStatement createUpdatePreparedStatement(Connection conn, NDCCode toChange) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("UPDATE ndcodes SET Description=? WHERE Code=?");
        pstring.setString(1, toChange.getDescription());
        pstring.setString(2, toChange.getCode());
        return pstring;
    }

    public List<NDCCode> getCodesWithFilter(String filter) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = creategetCodesWithFilterPreparedStatement(conn, filter);
                ResultSet rs = pstring.executeQuery()){
            return loadResults(rs);
        }
    }

    private PreparedStatement creategetCodesWithFilterPreparedStatement(Connection conn, String filter) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM ndcodes WHERE Code LIKE ?");
        pstring.setString(1, "%" + filter + "%");
        return pstring;
    }
}
