package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.AllergyBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class AllergyDAO {

	private DAOFactory factory;
	private transient final AllergyBeanLoader allergyBeanLoader = new AllergyBeanLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public AllergyDAO(final DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of patient's allergies.
	 * 
	 * @param pid
	 *            A long for the MID of the patient we are looking up.
	 * @return A java.util.List of AllergyBeans associated with this patient.
	 * @throws DBException
	 */
	public List<AllergyBean> getAllergies(final long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM allergies WHERE PatientID=? ORDER BY FirstFound DESC")) {
			stmt.setLong(1, pid);
			final ResultSet results = stmt.executeQuery();
			final List<AllergyBean> loadlist = allergyBeanLoader.loadList(results);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds an allergy to this patient's list.
	 * 
	 * @param allergy
	 *            allergy bean
	 * @throws DBException
	 */
	public void addAllergy(final AllergyBean allergy) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO allergies(PatientID, Code, FirstFound, Description) VALUES (?,?,?,?)")) {
			stmt.setLong(1, allergy.getPatientID());
			stmt.setString(2, allergy.getNDCode());
			if (allergy.getFirstFound() == null) {
				stmt.setDate(3, null);
			} else {
				stmt.setDate(3, new java.sql.Date(allergy.getFirstFound().getTime()));
			}
			stmt.setString(4, allergy.getDescription());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
