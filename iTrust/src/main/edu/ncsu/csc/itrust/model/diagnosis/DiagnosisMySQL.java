package edu.ncsu.csc.itrust.model.diagnosis;

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

public class DiagnosisMySQL implements DiagnosisData {

	private DiagnosisSQLLoader loader;
	private DiagnosisValidator validator;
	private DataSource ds;

	/**
	 * Constructor of DiagnosisMySQL instance.
	 * 
	 * @throws DBException when data source cannot be created from the given context names
	 */
	public DiagnosisMySQL() throws DBException {
		loader = new DiagnosisSQLLoader();
		try {
			this.ds = getDataSource();
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new DiagnosisValidator();
	}
	
	public Context getContext() throws NamingException {
		return new InitialContext();
	}
	
	public DataSource getDataSource() throws NamingException {
		return ((DataSource) (((Context) getContext().lookup("java:comp/env"))).lookup("jdbc/itrust"));
	}
	
	/**
	 * Constructor for testing.
	 * 
	 * @param ds
	 * 		testing data source
	 */
	public DiagnosisMySQL(DataSource ds) {
		loader = new DiagnosisSQLLoader();
		this.ds = ds;
		validator = new DiagnosisValidator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Diagnosis> getAll() throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM diagnosis, icdcode where icdcode = code");
				ResultSet rs = ps.executeQuery()) {
			return loader.loadList(rs);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Diagnosis getByID(long id) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = createGetByIdStatement(conn, id);
				ResultSet rs = ps.executeQuery()) {
			return rs.next() ? loader.loadSingle(rs) : null;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * @param conn 
	 * 			database connection
	 * @param id 
	 * 			id of diagnosis
	 * @return statement to retrieve diagnosis by id
	 * @throws SQLException	when error occurs when querying db
	 */
	private PreparedStatement createGetByIdStatement(Connection conn, long id) throws SQLException {
		PreparedStatement result = conn.prepareStatement("SELECT * FROM diagnosis, icdcode where icdcode = code AND id = ?");
		result.setLong(1, id);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Diagnosis addObj) throws DBException {
		try {
			validator.validate(addObj);
		} catch (FormValidationException e) {
			throw new DBException(new SQLException(e.getMessage()));
		}
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = loader.loadParameters(conn, null, addObj, true);) {
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(Diagnosis updateObj) throws DBException {
		try {
			validator.validate(updateObj);
		} catch (FormValidationException e) {
			throw new DBException(new SQLException(e.getMessage()));
		}
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = loader.loadParameters(conn, null, updateObj, false);) {
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public boolean remove(long diagnosisId) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = createRemoveStatement(conn, diagnosisId)) {
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	private PreparedStatement createRemoveStatement(Connection conn, long id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM diagnosis WHERE id = ?");
		ps.setLong(1, id);
		return ps;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Diagnosis> getAllEmergencyDiagnosis(long mid) throws SQLException {
		try (Connection conn = ds.getConnection();
			PreparedStatement statement = createEmergencyDiagnosisPreparedStatement(conn, mid);
			ResultSet resultSet = statement.executeQuery()) {
			return loader.loadList(resultSet);
		}
	}
	
	public PreparedStatement createEmergencyDiagnosisPreparedStatement(Connection conn, long mid) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(
				"SELECT d.id, d.visitId, d.icdCode, c.name, c.is_chronic FROM diagnosis d, icdcode c, officevisit ov "
					 + "WHERE d.visitId = ov.visitID AND ov.patientMID = ? AND d.icdCode = c.code "
					 + "AND (c.is_chronic OR ov.visitDate >= DATE_SUB(NOW(), INTERVAL 30 DAY)) "
					 + "ORDER BY ov.visitDate DESC");
		ps.setLong(1, mid);
		return ps;
	}

	@Override
	public List<Diagnosis> getAllDiagnosisByOfficeVisit(long visitId) throws DBException {
		try (Connection conn = ds.getConnection();
				PreparedStatement ps = createGetByOfficeVisitStatement(conn, visitId);
				ResultSet rs = ps.executeQuery()) {
			return loader.loadList(rs);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	public PreparedStatement createGetByOfficeVisitStatement(Connection conn, long visitId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM diagnosis, icdcode where icdcode = code AND visitId = ?");
		ps.setLong(1, visitId);
		return ps;
	}
}
