package edu.ncsu.csc.itrust.model.immunization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

public class ImmunizationMySQL implements ImmunizationData {

	private ImmunizationSQLLoader loader;
	private ImmunizationValidator validator;
	private DataSource ds;

	/**
	 * Constructor of ImmunizationsMySQL instance.
	 * 
	 * @throws DBException when data source cannot be created from the given context names
	 */
	public ImmunizationMySQL() throws DBException {
		loader = new ImmunizationSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new ImmunizationValidator(this.ds);
	}
	
	/**
	 * Constructor for testing.
	 * 
	 * @param ds testing data source
	 */
	public ImmunizationMySQL(DataSource ds) {
		loader = new ImmunizationSQLLoader();
		this.ds = ds;
		validator = new ImmunizationValidator(this.ds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Immunization> getAll() throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement pstring = conn.prepareStatement("SELECT * FROM immunization, cptCode WHERE code=cptcode");
				ResultSet results = pstring.executeQuery()) {
			List<Immunization> list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Immunization getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM immunization, cptCode WHERE id="+id+" AND code=cptcode");
			ResultSet resultSet = statement.executeQuery()) {
			List<Immunization> immunizationList = loader.loadList(resultSet);
			if (immunizationList.size() > 0) {
				return immunizationList.get(0);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Immunization addObj) throws DBException {
		try {
			validator.validate(addObj);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1));
		}
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = loader.loadParameters(conn, null, addObj, true);) {
			int results = statement.executeUpdate();
			return results == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(Immunization updateObj) throws DBException {
		try {
			validator.validate(updateObj);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1.getMessage()));
		}

		try (Connection conn = ds.getConnection();
			PreparedStatement statement = loader.loadParameters(conn, null, updateObj, false);) {
			int results = statement.executeUpdate();
			return results == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Immunization> getAllImmunizations(long mid) throws DBException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = createImmunizationPreparedStatement(conn, mid);
			ResultSet resultSet = statement.executeQuery()) {
			List<Immunization> list = loader.loadList(resultSet);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public PreparedStatement createImmunizationPreparedStatement(Connection conn, long mid) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("i.id, ");
		sb.append("i.visitId, ");
		sb.append("i.cptCode, ");
		sb.append("c.name ");
		sb.append("FROM ");
		sb.append("immunization i, ");
		sb.append("cptcode c, ");
		sb.append("officevisit ov ");
		sb.append("WHERE i.visitId = ov.visitID ");
		sb.append("AND ov.patientMID = ? ");
		sb.append("AND i.cptCode = c.code ");
		
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		ps.setLong(1, mid);
		return ps;
	}
	
	public boolean remove(long id) throws SQLException {
	    try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createRemovePreparedStatement(conn, id);){
            return pstring.executeUpdate() > 0;
        }
	}

    private PreparedStatement createRemovePreparedStatement(Connection conn, long id) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("DELETE FROM immunization WHERE id=?");
        pstring.setLong(1, id);
        return pstring;
    }

    public List<Immunization> getImmunizationsForOfficeVisit(long officeVisitId) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createOVPreparedStatement(conn, officeVisitId);
                ResultSet results = pstring.executeQuery()){
            return loader.loadList(results);
        }
    }

    private PreparedStatement createOVPreparedStatement(Connection conn, long officeVisitId) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement("SELECT * FROM immunization, cptcode WHERE cptCode=code AND visitId=?");

        pstring.setLong(1, officeVisitId);
        return pstring;
    }

    public List<Immunization> getImmunizationsByMID(long mid) throws SQLException {
        try (Connection conn = ds.getConnection();
                PreparedStatement pstring = createGetByMIDStatement(conn, mid);
                ResultSet results = pstring.executeQuery()) {
            return loader.loadList(results);
        }
    }

    private PreparedStatement createGetByMIDStatement(Connection conn, long mid) throws SQLException {
        PreparedStatement pstring = conn.prepareStatement(
                "SELECT * FROM immunization, cptcode, officevisit " + 
                "WHERE immunization.cptCode=cptcode.code " +
                "AND immunization.visitId=officevisit.visitID " +
                "AND officevisit.patientMID=?");

        pstring.setLong(1, mid);
        return pstring;
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