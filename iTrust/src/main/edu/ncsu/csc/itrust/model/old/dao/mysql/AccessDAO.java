package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * AccessDAO is for all queries related to authorization.
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
 * 
 * 
 * 
 */
@SuppressWarnings({})
public class AccessDAO {
	private transient final DAOFactory factory;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public AccessDAO(final DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the number of minutes it would take for a session to time out.
	 * This is done by effectively using the database table as a hash table. If
	 * a row in GlobalVariables does not exist, one is inserted with the default
	 * value '20'.
	 * 
	 * @return An int for the number of minutes.
	 * @throws DBException
	 */
	public int getSessionTimeoutMins() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT Value FROM globalvariables WHERE Name='Timeout'")) {
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int returnVal = rs.getInt("Value");
				return returnVal;
			} else {
				insertDefaultTimeout(20);
				return 20;
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Sets the number of minutes it would take for a session to timeout.
	 * 
	 * @param mins
	 *            An int specifying the number of minutes
	 * @throws DBException
	 */
	public void setSessionTimeoutMins(int mins) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE globalvariables SET Value=? WHERE Name='Timeout'")) {
			stmt.setInt(1, mins);
			int numUpdated = stmt.executeUpdate();
			if (numUpdated == 0) // no value in the table
				insertDefaultTimeout(mins);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	private void insertDefaultTimeout(final int mins) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO globalvariables(Name,Value) VALUES ('Timeout', ?)")) {
			stmt.setInt(1, mins);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
