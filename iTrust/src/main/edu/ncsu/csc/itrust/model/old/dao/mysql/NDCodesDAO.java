package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.MedicationBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.MedicationBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing the ND Codes.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 * 
 * The National Drug Code (NDC) is a universal product identifier used in the
 * United States for drugs intended for human use.
 * 
 * @see http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm
 */
public class NDCodesDAO {

	private DAOFactory factory;
	private MedicationBeanLoader medicationLoader = new MedicationBeanLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public NDCodesDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all ND codes
	 * 
	 * @return A java.util.List of MedicationBeans.
	 * @throws DBException
	 */
	public List<MedicationBean> getAllNDCodes() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ndcodes ORDER BY CODE");
				ResultSet rs = stmt.executeQuery()) {
			List<MedicationBean> loadlist = medicationLoader.loadList(rs);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a particular description for a given code.
	 * 
	 * @param code
	 *            The ND code to be looked up.
	 * @return A bean representing the Medication that was looked up.
	 * @throws DBException
	 */
	public MedicationBean getNDCode(String code) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ndcodes WHERE Code = ?")) {
			stmt.setString(1, code);
			ResultSet rs = stmt.executeQuery();

			MedicationBean result = rs.next() ? medicationLoader.loadSingle(rs) : null;
			rs.close();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds a new ND code, returns whether or not the change was made. If the
	 * code already exists, an iTrustException is thrown.
	 * 
	 * @param med
	 *            The medication bean to be added.
	 * @return A boolean indicating success or failure.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addNDCode(MedicationBean med) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO ndcodes (Code, Description) " + "VALUES (?,?)")) {
			stmt.setString(1, med.getNDCode());
			stmt.setString(2, med.getDescription());
			boolean successfullyAdded = stmt.executeUpdate() == 1;
			return successfullyAdded;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new ITrustException("Error: Code already exists.");
			} else {
				throw new DBException(e);
			}
		}
	}

	/**
	 * Updates a particular code's description
	 * 
	 * @param med
	 *            A bean representing the particular medication to be updated.
	 * @return An int representing the number of updated rows.
	 * @throws DBException
	 */
	public int updateCode(MedicationBean med) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("UPDATE ndcodes SET Description = ? " + "WHERE Code = ?")) {
			stmt.setString(1, med.getDescription());
			stmt.setString(2, med.getNDCode());

			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Removes a ND code, returns whether or not the change was made.
	 * 
	 * @param med
	 *            The medication bean to be removed.
	 * @return A boolean indicating success or failure.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean removeNDCode(MedicationBean med) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM ndcodes WHERE Code=?")) {
			stmt.setString(1, med.getNDCode());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
