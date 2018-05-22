package edu.ncsu.csc.itrust.model.medicalProcedure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.cptcode.CPTCode;

public class MedicalProcedureMySQL {
    private DataSource ds;
    private MedicalProcedureValidator validator;
    
    public MedicalProcedureMySQL() throws DBException {
        try {
            this.ds = getDataSource();
            this.validator = new MedicalProcedureValidator();
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
    }
    
    protected DataSource getDataSource() throws NamingException {
        Context ctx = new InitialContext();
        return ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
    }
    
    public MedicalProcedureMySQL(DataSource ds) {
        this.ds = ds;
        this.validator = new MedicalProcedureValidator();
    }
    
    public boolean add(MedicalProcedure p) throws SQLException{
        try {
            validator.validate(p);
        } catch (FormValidationException e) {
            throw new SQLException(e.getMessage());
        }
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createAddPreparedStatement(conn, p);){
            return pstring.executeUpdate() > 0;
        }
    }

    private PreparedStatement createAddPreparedStatement(Connection conn, MedicalProcedure p) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("INSERT INTO medicalProcedure" +
                "(visitId, cptCode) " +
                "VALUES(?, ?)");
        pstring.setLong(1, p.getOfficeVisitId());
        pstring.setString(2, p.getCode());
        return pstring;
    }
    
    public MedicalProcedure get(long id) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetStatement(conn, id);
                ResultSet results = pstring.executeQuery()) {
            return loadSingle(results);
        }
    }

    private PreparedStatement createGetStatement(Connection conn, long id) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM medicalProcedure, cptcode " +
                "WHERE cptcode.code=medicalProcedure.cptCode " +
                "AND id=?");
        pstring.setLong(1, id);
        return pstring;
    }

    private MedicalProcedure loadSingle(ResultSet results) throws SQLException {
        if (results.next()){
            MedicalProcedure loadedProc = new MedicalProcedure();
            loadedProc.setId(results.getLong("id"));
            loadedProc.setOfficeVisitId(results.getLong("visitId"));
            loadedProc.setCptCode(new CPTCode(results.getString("code"), results.getString("name")));
            return loadedProc;
        } else {
            return null;
        }
    }
    
    public boolean update(MedicalProcedure p) throws SQLException{
        try {
            validator.validate(p);
        } catch (FormValidationException e) {
            throw new SQLException(e.getMessage());
        }
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createUpdatePreparedStatement(conn, p);){
            return pstring.executeUpdate() > 0;
        }
    }

    private PreparedStatement createUpdatePreparedStatement(Connection conn, MedicalProcedure p) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("UPDATE medicalProcedure SET visitId=?, cptCode=? WHERE id=?");
        pstring.setLong(1, p.getOfficeVisitId());
        pstring.setString(2, p.getCode());
        pstring.setLong(3, p.getId());
        return pstring;
    }
    
    public boolean remove(long id) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createRemovePreparedStatement(conn, id);){
            return pstring.executeUpdate() > 0;
        }
    }

    private PreparedStatement createRemovePreparedStatement(Connection conn, long id) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM medicalProcedure WHERE id=?");
        pstring.setLong(1, id);
        return pstring;
    }
    
    public List<MedicalProcedure> getMedicalProceduresForOfficeVisit(long officeVisitId) throws SQLException{
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createOVPreparedStatement(conn, officeVisitId);
                ResultSet results = pstring.executeQuery()){
            return loadRecords(results);
        }
    }

    private PreparedStatement createOVPreparedStatement(Connection conn, long officeVisitId) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM medicalProcedure, cptCode WHERE medicalProcedure.cptCode=cptCode.code AND visitId=?");

        pstring.setLong(1, officeVisitId);
        return pstring;
    }

    private List<MedicalProcedure> loadRecords(ResultSet results) throws SQLException {
        List<MedicalProcedure> list = new LinkedList<>();
        while (results.next()){
            MedicalProcedure temp = new MedicalProcedure();
            temp.setId(results.getLong("id"));
            temp.setOfficeVisitId(results.getLong("visitId"));
            temp.setCptCode(new CPTCode(results.getString("cptCode"), results.getString("name")));
            list.add(temp);
        }
        return list;
    }

    public String getCodeName(String code) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetCodeNamePreparedStatement(conn, code);
                ResultSet rs = pstring.executeQuery()){
            
            if (!rs.next()) 
                return "";

            return rs.getString("name");
        }
    }

    private PreparedStatement createGetCodeNamePreparedStatement(Connection conn, String code) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT name FROM cptCode WHERE Code=?");
        pstring.setString(1, code);
        return pstring;
    }
}
