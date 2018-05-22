package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.OverrideReasonBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.DrugReactionOverrideBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing the Reason Codes.
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
 * The Override Reason Code (ORC) is a universal product identifier used in the
 * United States for drugs intended for human use.
 * 
 * @see http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm
 */
public class DrugReactionOverrideCodesDAO {

	private DAOFactory factory;
	private DrugReactionOverrideBeanLoader orcLoader = new DrugReactionOverrideBeanLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public DrugReactionOverrideCodesDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all ND codes
	 * 
	 * @return A java.util.List of OverrideReasonBeans.
	 * @throws DBException
	 */
	public List<OverrideReasonBean> getAllORCodes() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM drugreactionoverridecodes ORDER BY CODE");
				ResultSet rs = stmt.executeQuery()) {
			List<OverrideReasonBean> loadlist = orcLoader.loadList(rs);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a particular description for a given code.
	 * 
	 * @param code
	 *            The override reason code to be looked up.
	 * @return A bean representing the override reason that was looked up.
	 * @throws DBException
	 */
	public OverrideReasonBean getORCode(String code) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM drugreactionoverridecodes WHERE Code = ?")) {
			stmt.setString(1, code);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				OverrideReasonBean result = orcLoader.loadSingle(rs);
				rs.close();
				return result;
			}
			rs.close();
			return null;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds a new override reason code, returns whether or not the change was
	 * made. If the code already exists, an iTrustException is thrown.
	 * 
	 * @param orc
	 *            The overridereason bean to be added.
	 * @return A boolean indicating success or failure.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addORCode(OverrideReasonBean orc) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO drugreactionoverridecodes (Code, Description) " + "VALUES (?,?)")) {
			stmt.setString(1, orc.getORCode());
			stmt.setString(2, orc.getDescription());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062)
				throw new ITrustException("Error: Code already exists.");
			throw new DBException(e);
		}
	}

	/**
	 * Updates a particular code's description
	 * 
	 * @param orc
	 *            A bean representing the particular override reason to be
	 *            updated.
	 * @return An int representing the number of updated rows.
	 * @throws DBException
	 */
	public int updateCode(OverrideReasonBean orc) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("UPDATE drugreactionoverridecodes SET Description = ? " + "WHERE Code = ?")) {
			stmt.setString(1, orc.getDescription());
			stmt.setString(2, orc.getORCode());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

}
